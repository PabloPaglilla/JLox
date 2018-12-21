package com.pablopaglilla.jlox;

import java.util.HashMap;
import java.util.Map;

class Environment {

    private final Map<String, Object> values = new HashMap<>();
    final Environment enclosing;

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        if (hasDefined(name)) {
            return values.get(name.lexeme);
        }

        if (hasEnclosing()) return enclosing.get(name);

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if (hasDefined(name)) {
            values.put(name.lexeme, value);
            return;
        }

        if (hasEnclosing()) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    private boolean hasDefined(Token name) {
        return values.containsKey(name.lexeme);
    }

    private boolean hasEnclosing() { return enclosing != null; }
}
