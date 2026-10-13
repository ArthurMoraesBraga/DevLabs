"""Cache em memória com TTL para análises detalhadas e dados compartilhados."""
from __future__ import annotations

import hashlib
import threading
import time

TTL_SECONDS = 600
_lock = threading.RLock()
_entries = {}
_key_locks = {}


def cache_key(owner, repo, token, *parts):
    # Não armazena nem imprime o token de acesso.
    token_id = hashlib.sha256((token or '').encode()).hexdigest()[:16]
    return (owner.lower(), repo.lower(), token_id, *parts)


def get_or_compute(key, factory):
    with _lock:
        item = _entries.get(key)
        if item and item[0] > time.monotonic():
            return item[1]
        key_lock = _key_locks.setdefault(key, threading.Lock())
    with key_lock:
        with _lock:
            item = _entries.get(key)
            if item and item[0] > time.monotonic():
                return item[1]
        value = factory()
        with _lock:
            _entries[key] = (time.monotonic() + TTL_SECONDS, value)
        return value


def clear():
    with _lock:
        _entries.clear()
