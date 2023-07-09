# mcc
mcc is a minimal compiler which three of us, [Severin Strobl
@severinstrobl](https://github.com/severinstrobl), [Tobias Preclik
@tpreclik](https://github.com/tpreclik) and myself developed during a
university course "Compilerbau" (Compiler building) in our 5th semester.

It is built from the ground up from the grammar of the language (lexer/parser),
generating an AST, generating custom immediate code from the AST, optimizing
(variable placement/common subexpression eliminiation etc.) up to a point where
it emits assembly code for either i686 (Intel 32 bit) or Power PC. The
generated assembly output then can be compiled with gcc. Power PC has never
been tested on actual hardware (only on emulators), but Intel assembly was
working. On modern gcc versions, some testcases fail, unfortunately.

## Usage
Building can simply be done like this:

```
$ . config
$ make
```

Then, mcc can be used:

```
$ ./mcc --arch ia32 -S hallo_welt.S examples/hallo_welt.e

$ gcc -m32 -o hallo_welt hallo_welt.S
/usr/bin/ld: warning: /tmp/cc7uzDQn.o: missing .note.GNU-stack section implies executable stack
/usr/bin/ld: NOTE: This behaviour is deprecated and will be removed in a future version of the linker
/usr/bin/ld: /tmp/cc7uzDQn.o: warning: relocation in read-only section `.text'
/usr/bin/ld: warning: creating DT_TEXTREL in a PIE

$ ./hallo_welt
Hallo Welt!
```

## Dependencies
As parser generator, mcc uses
[JLex](https://courses.cs.cornell.edu/cs412/2000SP/resources/skeleton.html), an
educational parser generator from Cornell university (circa year 2000). For
convenience reasons, the generated lexer/parser code (`src/Yylex.java` and
`src/Parser.java`) have already been generated and checked in.

## License
GNU GPL-3.
