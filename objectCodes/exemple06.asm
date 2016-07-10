extern _printf

SECTION .data
teste: dd 0
cont: dd 0
intFormat: db "%d", 10, 0

SECTION .text
global _WinMain@16

_WinMain@16:
push ebp
mov ebp, esp
push dword 0
pop dword [teste]
push dword 1
pop dword [cont]
_while_0:
push dword [cont]
push dword 30
pop ebx
pop eax
cmp eax, ebx
jg _false_cmp_0
push dword 1
jmp _end_cmp_0
_false_cmp_0:
push dword 0
_end_cmp_0:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _end_while_0
push dword [cont]
push dword 1
pop ebx
pop eax
add eax, ebx
push eax
pop dword [cont]
push dword [cont]
push dword 10
pop ebx
pop eax
cmp eax, ebx
jne _false_cmp_1
push dword 1
jmp _end_cmp_1
_false_cmp_1:
push dword 0
_end_cmp_1:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _else_0
push dword 0
pop dword [teste]
jmp _end_while_0
jmp _end_if_0
_else_0:
push dword [cont]
push dword 1
pop ebx
pop eax
sub eax, ebx
push eax
push dword 2
pop ebx
pop eax
imul ebx
push eax
pop dword [cont]
push dword [cont]
push dword 8
pop ebx
pop eax
cmp eax, ebx
jne _false_cmp_2
push dword 1
jmp _end_cmp_2
_false_cmp_2:
push dword 0
_end_cmp_2:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _end_if_1
jmp _while_0
_end_if_1:
_end_if_0:
push dword [cont]
push dword 1
pop ebx
pop eax
add eax, ebx
push eax
pop dword [cont]
jmp _while_0
_end_while_0:
push dword [teste]
push dword intFormat
call _printf
add esp, 8
mov esp, ebp
pop ebp
ret



