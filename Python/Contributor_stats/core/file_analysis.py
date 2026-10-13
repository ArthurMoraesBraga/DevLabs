"""Helpers de estatísticas de arquivos."""
def top_files_areas(details, limit=10):
    return list(details.directories.items())[:limit]
