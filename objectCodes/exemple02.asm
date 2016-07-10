extern _printf

SECTION .data
a: dd 0
intFormat: db "%d", 10, 0

SECTION .text
global _WinMain@16

_WinMain@16:
push ebp
mov ebp, esp
push dword 16
pop dword [a]
call _main
push eax
push dword intFormat
call _printf
add esp, 8
mov esp, ebp
pop ebp
ret

_main:
push ebp
mov ebp, esp
sub esp, 4
push dword [a]
push dword 4
pop ebx
pop eax
idiv ebx
push eax
pop dword [ebp-4]
push dword [ebp-4]
pop eax
jmp _end_main
_end_main:
mov esp, ebp
pop ebp
ret



