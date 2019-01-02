#ifndef clox_stack_h
#define clox_stack_h

#include "value.h"

typedef struct {
	Value* values;
	Value* stackTop;
	int capacity;
} Stack;

void initStack(Stack *stack);
void freeStack(Stack *stack);
void stackPush(Stack *stack, Value value);
Value stackPop(Stack *stack);

#endif