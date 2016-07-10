extern _printf

SECTION .data
operacao: dd 0
intFormat: db "%d", 10, 0

SECTION .text
global _WinMain@16

_WinMain@16:
push ebp
mov ebp, esp
push dword 2
pop dword [operacao]
call _main
mov esp, ebp
pop ebp
ret

_add:
push ebp
mov ebp, esp
push dword [ebp+12]
push dword [ebp+8]
pop ebx
pop eax
add eax, ebx
push eax
pop eax
jmp _end_add
_end_add:
mov esp, ebp
pop ebp
ret

_sub:
push ebp
mov ebp, esp
push dword [ebp+12]
push dword [ebp+8]
pop ebx
pop eax
sub eax, ebx
push eax
pop eax
jmp _end_sub
_end_sub:
mov esp, ebp
pop ebp
ret

_mult:
push ebp
mov ebp, esp
push dword [ebp+12]
push dword [ebp+8]
pop ebx
pop eax
imul ebx
push eax
pop eax
jmp _end_mult
_end_mult:
mov esp, ebp
pop ebp
ret

_div:
push ebp
mov ebp, esp
push dword [ebp+8]
push dword 0
pop ebx
pop eax
cmp eax, ebx
jne _div_false_cmp_0
push dword 1
jmp _div_end_cmp_0
_div_false_cmp_0:
push dword 0
_div_end_cmp_0:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _div_else_0
push dword 0
pop eax
jmp _end_div
jmp _div_end_if_0
_div_else_0:
push dword [ebp+12]
push dword [ebp+8]
pop ebx
pop eax
idiv ebx
push eax
pop eax
jmp _end_div
_div_end_if_0:
_end_div:
mov esp, ebp
pop ebp
ret

_main:
push ebp
mov ebp, esp
sub esp, 4
sub esp, 4
push dword 10
pop dword [ebp-4]
push dword 25
pop dword [ebp-8]
push dword [operacao]
push dword 0
pop ebx
pop eax
cmp eax, ebx
jne _main_false_cmp_0
push dword 1
jmp _main_end_cmp_0
_main_false_cmp_0:
push dword 0
_main_end_cmp_0:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _main_else_0
push dword [ebp-4]
push dword [ebp-8]
call _add
add esp, 8
push eax
push dword intFormat
call _printf
add esp, 8
jmp _main_end_if_0
_main_else_0:
push dword [operacao]
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _main_false_cmp_1
push dword 1
jmp _main_end_cmp_1
_main_false_cmp_1:
push dword 0
_main_end_cmp_1:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _main_else_1
push dword [ebp-4]
push dword [ebp-8]
call _sub
add esp, 8
push eax
push dword intFormat
call _printf
add esp, 8
jmp _main_end_if_1
_main_else_1:
push dword [operacao]
push dword 2
pop ebx
pop eax
cmp eax, ebx
jne _main_false_cmp_2
push dword 1
jmp _main_end_cmp_2
_main_false_cmp_2:
push dword 0
_main_end_cmp_2:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _main_else_2
push dword [ebp-4]
push dword [ebp-8]
call _mult
add esp, 8
push eax
push dword intFormat
call _printf
add esp, 8
jmp _main_end_if_2
_main_else_2:
push dword [operacao]
push dword 3
pop ebx
pop eax
cmp eax, ebx
jne _main_false_cmp_3
push dword 1
jmp _main_end_cmp_3
_main_false_cmp_3:
push dword 0
_main_end_cmp_3:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _main_else_3
push dword [ebp-4]
push dword [ebp-8]
call _div
add esp, 8
push eax
push dword intFormat
call _printf
add esp, 8
jmp _main_end_if_3
_main_else_3:
push dword 0000
push dword intFormat
call _printf
add esp, 8
_main_end_if_3:
_main_end_if_2:
_main_end_if_1:
_main_end_if_0:
_end_main:
mov esp, ebp
pop ebp
ret



