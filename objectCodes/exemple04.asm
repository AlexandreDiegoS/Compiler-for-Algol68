extern _printf

SECTION .data
valor: dd 0
intFormat: db "%d", 10, 0

SECTION .text
global _WinMain@16

_WinMain@16:
push ebp
mov ebp, esp
push dword 8
pop dword [valor]
push dword [valor]
call _main
add esp, 4
mov esp, ebp
pop ebp
ret

_fatorialN:
push ebp
mov ebp, esp
sub esp, 4
push dword [ebp+8]
push dword 1
pop ebx
pop eax
cmp eax, ebx
jle _fatorialN_false_cmp_0
push dword 1
jmp _fatorialN_end_cmp_0
_fatorialN_false_cmp_0:
push dword 0
_fatorialN_end_cmp_0:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _fatorialN_else_0
push dword [ebp+12]
push dword [ebp+8]
pop ebx
pop eax
imul ebx
push eax
push dword [ebp+8]
push dword 1
pop ebx
pop eax
sub eax, ebx
push eax
call _fatorialN
add esp, 8
push eax
pop dword [ebp-4]
jmp _fatorialN_end_if_0
_fatorialN_else_0:
push dword [ebp+12]
pop dword [ebp-4]
_fatorialN_end_if_0:
push dword [ebp-4]
pop eax
jmp _end_fatorialN
_end_fatorialN:
mov esp, ebp
pop ebp
ret

_fatorial:
push ebp
mov ebp, esp
push dword [ebp+8]
push dword 1
pop ebx
pop eax
cmp eax, ebx
jle _fatorial_false_cmp_0
push dword 1
jmp _fatorial_end_cmp_0
_fatorial_false_cmp_0:
push dword 0
_fatorial_end_cmp_0:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _fatorial_else_0
push dword 1
push dword [ebp+8]
call _fatorialN
add esp, 8
push eax
pop eax
jmp _end_fatorial
jmp _fatorial_end_if_0
_fatorial_else_0:
push dword [ebp+8]
push dword 0
pop ebx
pop eax
cmp eax, ebx
jne _fatorial_false_cmp_1
push dword 1
jmp _fatorial_end_cmp_1
_fatorial_false_cmp_1:
push dword 0
_fatorial_end_cmp_1:
push dword 1
pop ebx
pop eax
cmp eax, ebx
jne _fatorial_else_1
push dword 0
pop eax
jmp _end_fatorial
jmp _fatorial_end_if_1
_fatorial_else_1:
push dword [ebp+8]
pop eax
jmp _end_fatorial
_fatorial_end_if_1:
_fatorial_end_if_0:
_end_fatorial:
mov esp, ebp
pop ebp
ret

_main:
push ebp
mov ebp, esp
push dword [ebp+8]
call _fatorial
add esp, 4
push eax
push dword intFormat
call _printf
add esp, 8
_end_main:
mov esp, ebp
pop ebp
ret



