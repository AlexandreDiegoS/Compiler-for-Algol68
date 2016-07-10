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
pop dword [b]
pop dword [a]
mov esp, ebp
pop ebp
ret


