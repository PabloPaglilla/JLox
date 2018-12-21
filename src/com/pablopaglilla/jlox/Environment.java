package com.pablopaglilla.jlox;

import java.util.HashMap;
import java.util.Map;

class Environment {

    private final Map<String, Object> values = new HashMap<>();

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        if (isDefined(name)) {
            return values.get(name.lexeme);
        }

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    void assign(Token name, Object value) {
        if (isDefined(name)) {
            values.put(name.lexeme, value);
            return;
        }

        throw new RuntimeError(name,
                "Undefined variable '" + name.lexeme + "'.");
    }

    private boolean isDefined(Token name) {
        return values.containsKey(name.lexeme);
    }
}
