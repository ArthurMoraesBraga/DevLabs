
from collections import Counter
from dataclasses import dataclass, field
from datetime import datetime
from pathlib import PurePosixPath

from .github_client import GitHubClient
from .exceptions import GitHubAPIError
from .detail_cache import cache_key, get_or_compute

EXTENSIONS = {'.py':'Python','.java':'Java','.js':'JavaScript','.jsx':'JavaScript (JSX)',
 '.ts':'TypeScript','.tsx':'TypeScript (TSX)','.html':'HTML','.css':'CSS','.scss':'SCSS',
 '.json':'JSON','.md':'Markdown','.sql':'SQL','.c':'C','.cpp':'C++','.h':'C/C++',
 '.cs':'C#','.go':'Go','.rs':'Rust','.php':'PHP','.rb':'Ruby','.kt':'Kotlin',
 '.swift':'Swift','.vue':'Vue','.sh':'Shell','.yml':'YAML','.yaml':'YAML','.xml':'XML'}

@dataclass
class DetailStats:
    username: str
    prs_open: int = 0
    prs_merged: int = 0
    prs_closed_unmerged: int = 0
    prs_approved: int = 0  # PRs distintos com ao menos uma aprovação
    reviews: int = 0
    issues_opened: int = 0
    issues_closed: int = 0  # fechadas e abertas pelo próprio usuário, NÃO autoria da resolução
    files_changed: int = 0
    directories: dict = field(default_factory=dict)
    languages: dict = field(default_factory=dict)
    timeline: dict = field(default_factory=dict)
    warnings: list = field(default_factory=list)


def _pages(client, path, params=None, max_pages=10):
    """Pagina até max_pages; sinaliza truncamento para não apresentar números como totais."""
    results = []
    for page in range(1, max_pages + 1):
        p = dict(params or {}, per_page=100, page=page)
        response = client._get(path, p)
        if response.status_code != 200:
            raise client._error_from_response(response)
        chunk = response.json()
        if not isinstance(chunk, list):
            raise GitHubAPIError('Resposta inesperada da API GitHub.')
        results.extend(chunk)
        if len(chunk) < 100:
            return results, False
    return results, True


def _shared_repo_data(client, base, max_pages):
    """Issues, PRs e revisões são comuns a TODOS os colaboradores.

    Cada PR é consultado uma única vez por repositório dentro do TTL.
    """
    issues, issues_truncated = _pages(client, f'{base}/issues', {'state': 'all'}, max_pages)
    prs, prs_truncated = _pages(client, f'{base}/pulls', {'state': 'all'}, max_pages)
    review_counts = Counter()
    approvals = {}
    review_warnings = []
    for pr in prs:
        number = pr['number']
        reviews, truncated = _pages(client, f'{base}/pulls/{number}/reviews', max_pages=2)
        if truncated:
            review_warnings.append(f'Revisões do PR #{number}: dados parciais.')
        for review in reviews:
            login = (review.get('user') or {}).get('login', '').lower()
            if not login:
                continue
            state = review.get('state')
            if state in ('APPROVED', 'CHANGES_REQUESTED', 'COMMENTED'):
                review_counts[login] += 1
            if state == 'APPROVED':
                approvals.setdefault(login, set()).add(number)
    return issues, issues_truncated, prs, prs_truncated, review_counts, approvals, review_warnings


def analyze_contributor(owner, repo, username, token=None, client=None, max_pages=10, max_commits=250):
    """Análise completa com cache TTL e reutilização das consultas do repositório."""
    injected_client = client is not None
    client = client or GitHubClient(token)
    base = f'/repos/{owner}/{repo}'
    user_key = cache_key(owner, repo, token, 'contributor', username.lower(), max_pages, max_commits)
    shared_key = cache_key(owner, repo, token, 'shared', max_pages)

    def compute():
        result = DetailStats(username=username)
        issues, issues_truncated, prs, prs_truncated, review_counts, approvals, review_warnings = get_or_compute(
            shared_key, lambda: _shared_repo_data(client, base, max_pages)
        )
        if issues_truncated:
            result.warnings.append('Issues: limite de paginação atingido; contagem parcial.')
        if prs_truncated:
            result.warnings.append('Pull requests: limite de paginação atingido; contagem parcial.')
        result.warnings.extend(review_warnings)
        login = username.lower()
        for issue in issues:
            if 'pull_request' in issue:
                continue
            if (issue.get('user') or {}).get('login', '').lower() == login:
                result.issues_opened += 1
                if issue.get('state') == 'closed':
                    result.issues_closed += 1
        for pr in prs:
            if (pr.get('user') or {}).get('login', '').lower() != login:
                continue
            if pr.get('state') == 'open':
                result.prs_open += 1
            elif pr.get('merged_at'):
                result.prs_merged += 1
            else:
                result.prs_closed_unmerged += 1
        result.reviews = review_counts[login]
        result.prs_approved = len(approvals.get(login, ()))

        commits, truncated = _pages(client, f'{base}/commits', {'author': username}, max_pages)
        if truncated:
            result.warnings.append('Commits: limite de paginação atingido; dados parciais.')
        if len(commits) > max_commits:
            result.warnings.append(f'Arquivos: amostra dos {max_commits} commits mais recentes.')
        paths = set()
        directories, languages, timeline = Counter(), Counter(), Counter()
        for commit in commits:
            dt = ((commit.get('commit') or {}).get('author') or {}).get('date')
            if dt:
                try:
                    timeline[datetime.fromisoformat(dt.replace('Z', '+00:00')).strftime('%Y-%m')] += 1
                except ValueError:
                    pass
        for commit in commits[:max_commits]:
            sha = commit.get('sha')
            if not sha:
                continue
            response = client._get(f'{base}/commits/{sha}')
            if response.status_code != 200:
                raise client._error_from_response(response)
            for f in response.json().get('files', []):
                path = f.get('filename', '')
                if not path:
                    continue
                paths.add(path)
                parts = PurePosixPath(path).parts
                directories[parts[0] if len(parts) > 1 else 'raiz'] += 1
                languages[EXTENSIONS.get(PurePosixPath(path).suffix.lower(), 'Outros')] += 1
        result.files_changed = len(paths)
        result.directories = dict(directories.most_common())
        result.languages = dict(languages.most_common())
        result.timeline = dict(sorted(timeline.items()))
        return result

    # Clientes injetados são usados em testes e não compartilham cache global.
    if injected_client:
        return compute()
    return get_or_compute(user_key, compute)
