extern _printf

SECTION .data
a: dd 0
b: dd 0
intFormat: db "%d", 10, 0

SECTION .text
global _WinMain@16

_WinMain@16:
push ebp
mov ebp, esp
push dword 0
pop dword [a]
push dword 1
pop dword [b]
call _main
mov esp, ebp
pop ebp
ret

_fib:
push ebp
mov ebp, esp
push dword [ebp+8]
push dword 0
pop ebx
pop eax
cmp eax, ebx
jne _fib_false_cmp_0
push dword 1
jmp _fib_end_cmp_0
_fib_false_cmp_0:
push dword 0
_fib_end_cmp_0:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _fib_else_0
push dword [a]
pop eax
jmp _end_fib
jmp _fib_end_if_0
_fib_else_0:
push dword [ebp+8]
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _fib_false_cmp_1
push dword 1
jmp _fib_end_cmp_1
_fib_false_cmp_1:
push dword 0
_fib_end_cmp_1:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _fib_else_1
push dword [b]
pop eax
jmp _end_fib
jmp _fib_end_if_1
_fib_else_1:
sub esp, 12
push dword 1
pop dword [ebp-12]
push dword [a]
pop dword [ebp-4]
push dword [b]
pop dword [ebp-8]
_fib_while_0:
push dword [ebp-12]
push dword [ebp+8]
pop ebx
pop eax
cmp eax, ebx
je _fib_false_cmp_2
push dword 1
jmp _fib_end_cmp_2
_fib_false_cmp_2:
push dword 0
_fib_end_cmp_2:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _fib_end_while_0
sub esp, 4
push dword [ebp-12]
push dword 1
pop ebx
pop eax
add eax, ebx
push eax
pop dword [ebp-12]
push dword [ebp-4]
pop dword [ebp-16]
push dword [ebp-8]
pop dword [ebp-4]
push dword [ebp-16]
push dword [ebp-4]
pop ebx
pop eax
add eax, ebx
push eax
pop dword [ebp-8]
jmp _fib_while_0
_fib_end_while_0:
push dword [ebp-8]
pop eax
jmp _end_fib
_fib_end_if_1:
_fib_end_if_0:
_end_fib:
mov esp, ebp
pop ebp
ret

_main:
push ebp
mov ebp, esp
push dword 15
call _fib
add esp, 4
push eax
push dword intFormat
call _printf
add esp, 8
_end_main:
mov esp, ebp
pop ebp
ret



