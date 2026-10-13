
from datetime import datetime
from xml.sax.saxutils import escape

from reportlab.platypus import (
    SimpleDocTemplate,
    Paragraph,
    Spacer,
    Table,
    TableStyle,
)
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_CENTER
from reportlab.lib.units import cm


DARK = colors.HexColor("#10141F")
CARD = colors.HexColor("#1B2232")
WHITE = colors.HexColor("#FFFFFF")
MUTED = colors.HexColor("#A9B5C8")
BLUE = colors.HexColor("#678DFF")
GREEN = colors.HexColor("#45D6A0")
RED = colors.HexColor("#FF7487")


def _chart_bars(items, width=450, height=125):
    """Pequeno gráfico vetorial para relatórios PDF."""
    from reportlab.graphics.shapes import Drawing, Rect, String

    chart = Drawing(width, height)
    values = list(items)[-10:]

    if not values:
        return chart

    maximum = max(1, *(float(v) for _, v in values))
    step = (width - 20) / len(values)

    for i, (label, value) in enumerate(values):
        bar_h = (height - 40) * max(0, float(value)) / maximum
        x = 10 + i * step

        chart.add(
            Rect(
                x, 24, max(4, step - 9), bar_h,
                fillColor=BLUE,
                strokeColor=None,
            )
        )
        chart.add(
            String(
                x, 10, str(label)[-9:],
                fontSize=7,
                fillColor=DARK,
            )
        )
        chart.add(
            String(
                x, 28 + bar_h, str(value),
                fontSize=7,
                fillColor=DARK,
            )
        )

    return chart


def gerar_pdf(contributor, repositorio, caminho, details=None):
    """Gera um relatório PDF individual."""

    documento = SimpleDocTemplate(
        caminho,
        pagesize=(21 * cm, 29.7 * cm),
        rightMargin=2 * cm,
        leftMargin=2 * cm,
        topMargin=2 * cm,
        bottomMargin=2 * cm,
    )

    estilos = getSampleStyleSheet()

    titulo = ParagraphStyle(
        "TituloCustom",
        parent=estilos["Title"],
        fontName="Helvetica-Bold",
        fontSize=21,
        leading=27,
        textColor=WHITE,
        spaceAfter=10,
    )

    subtitulo = ParagraphStyle(
        "SubtituloCustom",
        parent=estilos["Normal"],
        fontSize=10,
        leading=15,
        textColor=MUTED,
    )

    secao = ParagraphStyle(
        "SecaoCustom",
        parent=estilos["Heading2"],
        fontSize=14,
        leading=19,
        textColor=DARK,
        spaceAfter=12,
    )

    label = ParagraphStyle(
        "CardLabelCustom",
        parent=estilos["Normal"],
        fontSize=9,
        leading=14,
        textColor=MUTED,
        alignment=TA_CENTER,
    )

    numero = ParagraphStyle(
        "CardNumeroCustom",
        parent=estilos["Normal"],
        fontName="Helvetica-Bold",
        fontSize=20,
        leading=27,
        textColor=WHITE,
        alignment=TA_CENTER,
    )

    normal = ParagraphStyle(
        "TextoCustom",
        parent=estilos["Normal"],
        fontSize=10,
        leading=17,
        textColor=DARK,
    )

    elementos = []

    cabecalho = Table(
        [[[
            Paragraph("GITHUB CONTRIBUTOR STATS", titulo),
            Paragraph("Relatório individual de contribuições", subtitulo),
            Spacer(1, 12),
            Paragraph(
                f"Colaborador: {escape(contributor.username)}",
                subtitulo,
            ),
            Paragraph(
                f"Repositório: {escape(str(repositorio))}",
                subtitulo,
            ),
        ]]],
        colWidths=[17 * cm],
    )

    cabecalho.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), DARK),
        ("LEFTPADDING", (0, 0), (-1, -1), 22),
        ("RIGHTPADDING", (0, 0), (-1, -1), 22),
        ("TOPPADDING", (0, 0), (-1, -1), 22),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 22),
    ]))

    elementos.append(cabecalho)
    elementos.append(Spacer(1, 28))
    elementos.append(Paragraph("Resumo de contribuições", secao))

    def card(nome, valor, cor):
        return [
            Paragraph(nome, label),
            Spacer(1, 9),
            Paragraph(
                f'<font color="{cor.hexval()}">'
                f'{escape(str(valor))}</font>',
                numero,
            ),
        ]

    impacto = contributor.net_impact

    dados = [
        [
            card("COMMITS", f"{contributor.commits:,}", BLUE),
            card(
                "LINHAS ADICIONADAS",
                f"+{contributor.additions:,}",
                GREEN,
            ),
        ],
        [
            card(
                "LINHAS REMOVIDAS",
                f"-{contributor.deletions:,}",
                RED,
            ),
            card(
                "IMPACTO LÍQUIDO",
                f"{impacto:+,}",
                GREEN if impacto >= 0 else RED,
            ),
        ],
    ]

    tabela = Table(
        dados,
        colWidths=[8.35 * cm, 8.35 * cm],
        rowHeights=[3 * cm, 3 * cm],
        hAlign="CENTER",
    )

    tabela.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), CARD),
        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
        ("LEFTPADDING", (0, 0), (-1, -1), 14),
        ("RIGHTPADDING", (0, 0), (-1, -1), 14),
        ("LINEBELOW", (0, 0), (-1, 0), 6, WHITE),
        ("LINEBEFORE", (1, 0), (1, -1), 6, WHITE),
    ]))

    elementos.append(tabela)
    elementos.append(Spacer(1, 25))
    elementos.append(Paragraph("Atividade recente", secao))

    elementos.append(
        Paragraph(
            f"<b>Último commit:</b> "
            f"{escape(str(contributor.last_commit_display))}",
            normal,
        )
    )

    elementos.append(Spacer(1, 25))

    elementos.append(
        Paragraph(
            "O impacto líquido corresponde à diferença entre "
            "linhas adicionadas e removidas. Essa métrica não "
            "representa, isoladamente, a qualidade ou a "
            "complexidade do trabalho.",
            normal,
        )
    )

    elementos.append(Spacer(1, 15))
    elementos.extend(_detail_sections(details, secao, normal))

    def rodape(canvas, doc):
        canvas.saveState()
        largura = doc.pagesize[0]

        canvas.setStrokeColor(colors.HexColor("#DDE2EA"))
        canvas.line(
            2 * cm, 1.55 * cm,
            largura - 2 * cm, 1.55 * cm,
        )

        canvas.setFont("Helvetica", 8)
        canvas.setFillColor(colors.HexColor("#788397"))

        canvas.drawString(
            2 * cm,
            1.15 * cm,
            f"Gerado em {datetime.now():%d/%m/%Y às %H:%M}",
        )

        canvas.drawRightString(
            largura - 2 * cm,
            1.15 * cm,
            f"Página {doc.page}",
        )

        canvas.restoreState()

    documento.build(
        elementos,
        onFirstPage=rodape,
        onLaterPages=rodape,
    )


def _detail_sections(d, section, normal):
    """Tabelas e gráficos dos dados complementares disponíveis."""

    if d is None:
        return [
            Paragraph(
                "Dados complementares não disponíveis.",
                normal,
            )
        ]

    result = []

    metrics = [
        ("PRs abertos", d.prs_open),
        ("PRs mesclados", d.prs_merged),
        ("PRs fechados sem merge", d.prs_closed_unmerged),
        ("PRs aprovados pelo colaborador", d.prs_approved),
        ("Revisões realizadas", d.reviews),
        ("Issues abertas", d.issues_opened),
        (
            "Issues abertas pelo usuário e fechadas",
            d.issues_closed,
        ),
        ("Arquivos distintos alterados", d.files_changed),
    ]

    result.append(
        Paragraph("Pull Requests, revisões e Issues", section)
    )

    t = Table(
        [
            [Paragraph(escape(str(k)), normal), str(v)]
            for k, v in metrics
        ],
        colWidths=[12 * cm, 5 * cm],
        hAlign="LEFT",
    )

    t.setStyle(TableStyle([
        (
            "ROWBACKGROUNDS",
            (0, 0), (-1, -1),
            [WHITE, colors.HexColor("#F0F2F7")],
        ),
        ("TOPPADDING", (0, 0), (-1, -1), 6),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 6),
    ]))

    result.append(t)
    result.append(
        Paragraph("Evolução mensal dos commits", section)
    )

    timeline = sorted(d.timeline.items())

    if timeline:
        result.append(_chart_bars(timeline))

        result.append(
            Paragraph(
                "Histórico mensal: " + escape(
                    " | ".join(
                        f"{k}: {v}"
                        for k, v in timeline
                    )
                ),
                normal,
            )
        )
    else:
        result.append(
            Paragraph("Sem dados temporais disponíveis.", normal)
        )

    for title, data in [
        ("Linguagens dos arquivos modificados", d.languages),
        ("Pastas com alterações", d.directories),
    ]:
        result.append(Paragraph(title, section))

        if data:
            rows = [
                [Paragraph(escape(str(k)), normal), str(v)]
                for k, v in sorted(
                    data.items(),
                    key=lambda kv: -kv[1],
                )
            ]

            tab = Table(
                rows,
                colWidths=[12 * cm, 5 * cm],
                hAlign="LEFT",
            )

            tab.setStyle(TableStyle([
                (
                    "ROWBACKGROUNDS",
                    (0, 0), (-1, -1),
                    [WHITE, colors.HexColor("#F0F2F7")],
                ),
                ("TOPPADDING", (0, 0), (-1, -1), 5),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
            ]))

            result.append(tab)
        else:
            result.append(
                Paragraph("Sem dados disponíveis.", normal)
            )

    if d.warnings:
        result.append(
            Paragraph("Limitações da coleta", section)
        )

        for warning in dict.fromkeys(d.warnings):
            result.append(
                Paragraph(escape(str(warning)), normal)
            )

    return result


def gerar_pdf_geral(result, caminho, details_by_user=None):
    """PDF geral com ranking e detalhes de cada colaborador."""
    from reportlab.platypus import PageBreak

    doc = SimpleDocTemplate(
        caminho,
        pagesize=(21 * cm, 29.7 * cm),
        rightMargin=2 * cm,
        leftMargin=2 * cm,
        topMargin=2 * cm,
        bottomMargin=2 * cm,
    )

    st = getSampleStyleSheet()

    h = ParagraphStyle(
        "GeneralHeader",
        parent=st["Heading1"],
        textColor=BLUE,
        spaceAfter=14,
    )

    section = ParagraphStyle(
        "GeneralSection",
        parent=st["Heading2"],
        textColor=DARK,
        spaceBefore=15,
    )

    normal = ParagraphStyle(
        "GeneralNormal",
        parent=st["Normal"],
        leading=15,
        spaceAfter=6,
    )

    story = [
        Paragraph("GITHUB CONTRIBUTOR STATS", h),
        Paragraph("Relatório geral de contribuições", section),
        Paragraph(
            f"Repositório: {escape(result.full_name)}",
            normal,
        ),
        Spacer(1, 15),
        Paragraph(
            f"Colaboradores: {len(result.contributors)} | "
            f"Commits: {result.total_commits} | "
            f"Adições: {result.total_additions} | "
            f"Remoções: {result.total_deletions}",
            normal,
        ),
        Spacer(1, 20),
    ]

    rows = [[
        "Colaborador",
        "Commits",
        "Adições",
        "Remoções",
        "Impacto",
    ]]

    for c in result.contributors:
        rows.append([
            Paragraph(escape(c.username), normal),
            str(c.commits),
            str(c.additions),
            str(c.deletions),
            str(c.net_impact),
        ])

    table = Table(
        rows,
        repeatRows=1,
        colWidths=[
            6 * cm,
            2.5 * cm,
            2.8 * cm,
            2.8 * cm,
            2.9 * cm,
        ],
    )

    table.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, 0), DARK),
        ("TEXTCOLOR", (0, 0), (-1, 0), WHITE),
        (
            "ROWBACKGROUNDS",
            (0, 1), (-1, -1),
            [WHITE, colors.HexColor("#EDEFF5")],
        ),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 8),
        ("TOPPADDING", (0, 0), (-1, -1), 8),
        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
        ("FONTSIZE", (0, 0), (-1, -1), 8),
    ]))

    story.extend([
        table,
        Spacer(1, 20),
        Paragraph(
            "Comparativo de commits (10 primeiros)",
            section,
        ),
        _chart_bars([
            (c.username[:9], c.commits)
            for c in result.contributors[:10]
        ]),
    ])

    for c in result.contributors:
        d = (details_by_user or {}).get(c.username)

        story.extend([
            PageBreak(),
            Paragraph(
                f"Colaborador: {escape(c.username)}",
                h,
            ),
            Paragraph(
                f"Posição no ranking: {c.rank} | "
                f"Perfil: {escape(c.profile_url)}",
                normal,
            ),
            Paragraph(
                f"Commits: {c.commits} | "
                f"Linhas adicionadas: {c.additions} | "
                f"Linhas removidas: {c.deletions} | "
                f"Impacto líquido: {c.net_impact}",
                normal,
            ),
            Paragraph(
                "Último commit: "
                + escape(str(c.last_commit_display)),
                normal,
            ),
        ])

        story.extend(
            _detail_sections(d, section, normal)
        )

    story.extend([
        Spacer(1, 15),
        Paragraph(
            "Issues fechadas são contadas pelo autor da Issue, "
            "não necessariamente por quem a resolveu. "
            "A análise de arquivos e commits pode ser amostral "
            "conforme os avisos.",
            normal,
        ),
    ])

    def footer(canvas, doc):
        canvas.saveState()
        canvas.setFont("Helvetica", 8)
        canvas.setFillColor(colors.HexColor("#788397"))

        canvas.drawString(
            2 * cm,
            1.15 * cm,
            f"Gerado em {datetime.now():%d/%m/%Y %H:%M}",
        )

        canvas.drawRightString(
            doc.pagesize[0] - 2 * cm,
            1.15 * cm,
            f"Página {doc.page}",
        )

        canvas.restoreState()

    doc.build(
        story,
        onFirstPage=footer,
        onLaterPages=footer,
    )


def gerar_pdf_rapido(result, caminho):
    """PDF de ranking e gráfico, sem consultas adicionais à API."""
    from reportlab.platypus import KeepTogether
    doc = SimpleDocTemplate(caminho, pagesize=(21 * cm, 29.7 * cm),
                            rightMargin=2 * cm, leftMargin=2 * cm,
                            topMargin=2 * cm, bottomMargin=2 * cm)
    styles = getSampleStyleSheet()
    title = ParagraphStyle('QuickTitle', parent=styles['Heading1'], textColor=BLUE)
    normal = styles['Normal']
    story = [Paragraph('GITHUB CONTRIBUTOR STATS', title),
             Paragraph('Relatório rápido de contribuições', styles['Heading2']),
             Paragraph(f'Repositório: {escape(result.full_name)}', normal),
             Spacer(1, 12),
             Paragraph(f'Colaboradores: {len(result.contributors)} | '
                       f'Commits: {result.total_commits} | '
                       f'Adições: {result.total_additions} | '
                       f'Remoções: {result.total_deletions}', normal),
             Spacer(1, 12)]
    rows = [['#', 'Colaborador', 'Commits', 'Adições', 'Remoções', 'Impacto']]
    for c in result.contributors:
        rows.append([str(c.rank), Paragraph(escape(c.username), normal),
                     str(c.commits), str(c.additions), str(c.deletions), str(c.net_impact)])
    table = Table(rows, repeatRows=1, colWidths=[1*cm, 5.5*cm, 2.2*cm, 2.7*cm, 2.7*cm, 2.9*cm])
    table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), DARK),
        ('TEXTCOLOR', (0, 0), (-1, 0), WHITE),
        ('ROWBACKGROUNDS', (0, 1), (-1, -1), [WHITE, colors.HexColor('#EDEFF5')]),
        ('VALIGN', (0, 0), (-1, -1), 'MIDDLE'),
        ('TOPPADDING', (0, 0), (-1, -1), 6),
        ('BOTTOMPADDING', (0, 0), (-1, -1), 6),
    ]))
    story.extend([table, Spacer(1, 18),
                Paragraph('Comparativo de commits (Top 10)', styles['Heading2']),
                _chart_bars([(c.username[:9], c.commits) for c in
                            sorted(result.contributors, key=lambda c: c.commits, reverse=True)[:10]]),
                Spacer(1, 12),])
    doc.build(story)
