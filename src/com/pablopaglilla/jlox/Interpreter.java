package com.pablopaglilla.jlox;

import java.util.function.BiFunction;

public class Interpreter implements Expr.Visitor<Object> {

    void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case PLUS:
                if(areNumbers(left, right)) {
                    return (double)right + (double)left;
                }
                if(areStrings(left, right)) {
                    return (String)right + (String)left;
                }
                throw new RuntimeError(expr.operator,
                        "Operands must be two numbers or two strings.");
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            case GREATER:
                return doComparison(expr.operator, left, right,
                        (a, b) -> a > b,
                        (a, b) -> a.compareTo(b) > 0);
            case GREATER_EQUAL:
                return doComparison(expr.operator, left, right,
                        (a, b) -> a >= b,
                        (a, b) -> a.compareTo(b) >= 0);
            case LESS:
                return doComparison(expr.operator, left, right,
                        (a, b) -> a < b,
                        (a, b) -> a.compareTo(b) < 0);
            case LESS_EQUAL:
                return doComparison(expr.operator, left, right,
                        (a, b) -> a <= b,
                        (a, b) -> a.compareTo(b) <= 0);
            case BANG_EQUAL: return !isEqual(left, right);
            case EQUAL_EQUAL: return isEqual(left, right);
            case COMMA: return left;
        }

        // Unreachable.
        return null;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            case BANG:
                return !isTruthy(right);
        }

        // Unreacheable
        return null;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if(object == null) return false;
        if(object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        // nil is only equal to nil.
        if (a == null && b == null) return true;
        if (a == null) return false;

        return a.equals(b);
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        // Hack. Work around Java adding ".0" to integer-valued doubles.
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator,
                                     Object left, Object right) {
        if (areNumbers(left, right)) return;

        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private boolean doComparison(Token operator, Object left, Object right,
                                BiFunction<Double, Double, Boolean> numberHandler,
                                BiFunction<String, String, Boolean> stringHandler) {
        if(areNumbers(left, right)) return numberHandler.apply((double)left, (double)right);
        if(areStrings(left, right)) return stringHandler.apply((String)left, (String)right);

        throw new RuntimeError(operator, "Operands must be numbers or strings");
    }

    private boolean areNumbers(Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return true;
        return false;
    }

    private boolean areStrings(Object left, Object right) {
        if(left instanceof String && right instanceof String) return true;
        return false;
    }
}
