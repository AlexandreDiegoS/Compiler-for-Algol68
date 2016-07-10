extern _printf

SECTION .data
a: dd 0
b: dd 0
c: dd 0
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
push dword [a]
push dword [b]
pop ebx
pop eax
sub eax, ebx
push eax
pop dword [c]
push dword [a]
push dword [b]
pop ebx
pop eax
add eax, ebx
push eax
push dword intFormat
call _printf
add esp, 8
push dword [c]
pop eax
jmp _end_main
_end_main:
mov esp, ebp
pop ebp
ret



