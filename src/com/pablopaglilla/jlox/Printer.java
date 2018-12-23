package com.pablopaglilla.jlox;

import java.util.ArrayList;
import java.util.List;

public abstract class Printer implements Expr.Visitor<String>{

    static class AstPrinter extends Printer{

        String transform(String name, Expr... exprs) {
            StringBuilder builder = new StringBuilder();

            builder.append("(").append(name);
            for (Expr expr : exprs) {
                builder.append(" ");
                builder.append(expr.accept(this));
            }
            builder.append(")");

            return builder.toString();
        }

       public String visitGroupingExpr(Expr.Grouping expr) {
            return transform("group", expr.expression);
        }

    }

    static class RpnAstPrinter extends Printer {

        String transform(String name, Expr... exprs) {
            StringBuilder builder = new StringBuilder();

            for(Expr expr : exprs) {
                builder.append(" ");
                builder.append(expr.accept(this));
            }
            builder.append(" ").append(name);

            return builder.toString();
        }

        public String visitGroupingExpr(Expr.Grouping expr) {
            return expr.expression.accept(this);
        }

    }

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return transform(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public abstract String visitGroupingExpr(Expr.Grouping expr);

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return transform(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Expr.Variable expr) { return transform(expr.name.lexeme); }

    @Override
    public String visitAssignExpr(Expr.Assign expr) { return transform("= " + expr.name.lexeme, expr.value); }

    @Override
    public String visitLogicalExpr(Expr.Logical expr) { return transform(expr.operator.lexeme, expr.left, expr.right); }

    @Override
    public String visitCallExpr(Expr.Call expr) {
        return transform("call", expr.callee);
    }

    @Override
    public String visitAnnonymousExpr(Expr.Annonymous expr) {
        return transform("annonymous", expr.params.toArray(new Expr[expr.params.size()]));
    }

    abstract String transform(String name, Expr... exprs);

}
