package com.pablopaglilla.jlox;

public class Break extends RuntimeException {

    Token token;

    Break(Token token) {
        super();
        this.token = token;
    }

}
