#include "value.h"
#include "memory.h"
#include "stack.h"

void initStack(Stack *stack) {
	stack->capacity = GROW_CAPACITY(0);
	stack->values = GROW_ARRAY(stack->values, Value, 0,
		stack->capacity);
	stack->stackTop = stack->values;
}

void freeStack(Stack *stack) {
	FREE_ARRAY(Value, stack->values, stack->capacity);
	initStack(stack);
}

Value stackPop(Stack *stack) {
	stack->stackTop--;
	return *stack->stackTop;
}

void stackPush(Stack *stack, Value value) {
	if(stack->stackTop >= stack->values + stack->capacity) {
		int oldCapacity = stack->capacity;
		stack->capacity = GROW_CAPACITY(oldCapacity);
		stack->values = GROW_ARRAY(stack->values, Value,
			oldCapacity, stack->capacity);
		stack->stackTop = stack->values + oldCapacity;
	}

	*stack->stackTop = value;
	stack->stackTop++;
}