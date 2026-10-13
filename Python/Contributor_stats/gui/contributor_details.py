
"""Janela de análise individual, com carregamento assíncrono."""

import threading
import tkinter as tk
from tkinter import ttk, messagebox, filedialog

from core.contributor_analysis import analyze_contributor
from core.pdf_generator import gerar_pdf
from gui.activity_chart import ActivityChart


BG = "#10141F"
CARD = "#1B2232"
TEXT = "#F2F5FA"
MUTED = "#A9B5C8"
BLUE = "#678DFF"


class ContributorDetails(tk.Toplevel):

    def __init__(self, parent, contributor, repositorio, token=None):
        super().__init__(parent)

        self.contributor = contributor
        self.repositorio = repositorio
        self.token = token
        self.details = None

        self.title(f"Contribuições | {contributor.username}")
        self.geometry("940x740")
        self.minsize(780, 620)
        self.configure(bg=BG)
        self.transient(parent)

        header = tk.Frame(
            self,
            bg=BG,
            padx=22,
            pady=16,
        )
        header.pack(fill="x")

        tk.Label(
            header,
            text=contributor.username,
            bg=BG,
            fg=TEXT,
            font=("Segoe UI", 22, "bold"),
        ).pack(anchor="w")

        tk.Label(
            header,
            text=repositorio,
            bg=BG,
            fg=MUTED,
        ).pack(anchor="w")

        footer = tk.Frame(
            self,
            bg=BG,
            padx=22,
            pady=12,
        )
        footer.pack(side="bottom", fill="x")

        self.pdf_btn = tk.Button(
            footer,
            text="Exportar PDF individual",
            bg=BLUE,
            fg="white",
            relief="flat",
            font=("Segoe UI", 11, "bold"),
            command=self.exportar_pdf,
            pady=10,
        )
        self.pdf_btn.pack(fill="x")
        self.pdf_btn.configure(state="disabled")

        self.status = tk.Label(
            footer,
            text="Buscando PRs, issues, revisões e histórico...",
            bg=BG,
            fg=MUTED,
        )
        self.status.pack(pady=(8, 0))

        self.tabs = ttk.Notebook(self)
        self.tabs.pack(
            fill="both",
            expand=True,
            padx=22,
            pady=8,
        )

        self.overview = self._tab("Visão geral")
        self.collab = self._tab("PRs e Issues")
        self.evolution = self._tab("Evolução")
        self.files = self._tab("Arquivos e linguagens")

        self._text(
            self.overview,
            f"Commits: {contributor.commits}\n"
            f"Linhas adicionadas: +{contributor.additions:,}\n"
            f"Linhas removidas: -{contributor.deletions:,}\n"
            f"Impacto líquido: {contributor.net_impact:+,}\n"
            f"Último commit: {contributor.last_commit_display}",
            size=15,
        )

        self.collab_text = self._text(
            self.collab,
            "Carregando dados...",
            size=12,
        )

        self.chart = ActivityChart(self.evolution)
        self.chart.pack(
            fill="both",
            expand=True,
            padx=15,
            pady=15,
        )

        self.files_text = self._text(
            self.files,
            "Carregando dados...",
            size=12,
        )

        threading.Thread(
            target=self._load,
            daemon=True,
        ).start()

    def _tab(self, title):
        tab = tk.Frame(self.tabs, bg=BG)
        self.tabs.add(tab, text=title)
        return tab

    def _text(self, parent, value, size=12):
        label = tk.Label(
            parent,
            text=value,
            bg=BG,
            fg=TEXT,
            justify="left",
            anchor="nw",
            font=("Segoe UI", size),
            padx=22,
            pady=24,
            wraplength=750,
        )

        label.pack(fill="both", expand=True)
        return label

    def _load(self):
        try:
            owner, repo = self.repositorio.split("/", 1)

            data = analyze_contributor(
                owner,
                repo,
                self.contributor.username,
                self.token,
            )

        except Exception as exc:
            error = str(exc)

            self._safe_after(
                lambda: self.status.configure(
                    text=f"Falha na análise adicional: {error}"
                )
            )
            return

        self._safe_after(lambda: self._render(data))

    def _safe_after(self, callback):
        try:
            self.after(
                0,
                lambda: callback()
                if self.winfo_exists()
                else None,
            )
        except (tk.TclError, RuntimeError):
            pass

    def _render(self, d):
        self.details = d
        self.pdf_btn.configure(state="normal")

        self.collab_text.configure(
            text=(
                f"PRs abertos: {d.prs_open}\n"
                f"PRs mesclados: {d.prs_merged}\n"
                f"PRs fechados sem merge: {d.prs_closed_unmerged}\n"
                f"PRs aprovados pelo colaborador: {d.prs_approved}\n"
                f"Revisões realizadas: {d.reviews}\n"
                f"Issues abertas pelo colaborador: {d.issues_opened}\n"
                f"Issues do colaborador que foram fechadas: {d.issues_closed}\n\n"
            )
        )

        self.chart.set_series(d.timeline)

        areas = "\n".join(
            f"  {k}: {v} alterações"
            for k, v in list(d.directories.items())[:12]
        )

        langs = "\n".join(
            f"  {k}: {v} alterações"
            for k, v in list(d.languages.items())[:12]
        )

        self.files_text.configure(
            text=(
                f"Arquivos  modificados: "
                f"{d.files_changed}\n\n"
                f"Pastas mais alteradas:\n"
                f'{areas or "  Sem dados"}\n\n'
                f"Linguagens por extensão:\n"
                f'{langs or "  Sem dados"}'
            )
        )

        self.status.configure(
            text=(
                "Análise adicional concluída."
                if not d.warnings
                else "Avisos: " + " | ".join(d.warnings[:3])
            )
        )

    def exportar_pdf(self):
        if self.details is None:
            messagebox.showwarning(
                "Aguarde",
                "Espere a coleta complementar terminar "
                "antes de gerar o PDF.",
                parent=self,
            )
            return

        path = filedialog.asksaveasfilename(
            parent=self,
            defaultextension=".pdf",
            filetypes=[("PDF", "*.pdf")],
            initialfile=(
                f"{self.contributor.username}_contribuicoes.pdf"
            ),
        )

        if not path:
            return

        try:
            gerar_pdf(
                self.contributor,
                self.repositorio,
                path,
                details=self.details,
            )

            messagebox.showinfo(
                "PDF",
                "Relatório salvo com sucesso.",
                parent=self,
            )

        except Exception as exc:
            messagebox.showerror(
                "PDF",
                str(exc),
                parent=self,
            )
