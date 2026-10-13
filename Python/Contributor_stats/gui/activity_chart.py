"""Gráfico mensal com Canvas do Tkinter, sem dependência de matplotlib."""
import tkinter as tk

class ActivityChart(tk.Canvas):
    def __init__(self, parent, **kwargs):
        super().__init__(parent, bg='#1B2232', highlightthickness=0, height=240, **kwargs)
        self.series = {}
        self.bind('<Configure>', lambda event: self.redraw())

    def set_series(self, series):
        self.series = series or {}
        self.redraw()

    def redraw(self):
        self.delete('all')
        if not self.series:
            self.create_text(15, 40, text='Sem dados de atividade.', fill='#A9B5C8', anchor='w')
            return
        w, h = max(self.winfo_width(), 350), max(self.winfo_height(), 220)
        values = list(self.series.items())[-12:]
        peak = max(1, *(v for _, v in values))
        gap = (w-45)/max(len(values), 1)
        for i, (month, value) in enumerate(values):
            x = 25+i*gap
            bar_height = (h-75)*value/peak
            self.create_rectangle(x, h-42-bar_height, x+max(7,gap-12), h-42, fill='#678DFF', outline='')
            self.create_text(x+max(7,gap-12)/2, h-25, text=month[5:], fill='#A9B5C8', font=('Segoe UI', 9))
            self.create_text(x+max(7,gap-12)/2, h-50-bar_height, text=str(value), fill='#F2F5FA', font=('Segoe UI', 9))
