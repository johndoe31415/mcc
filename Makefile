EXAMPLES = $(shell find examples/ -name '*.e')
OBJS = $(shell find src/ -name '*.java' | grep -v 'src/mcc.java')
OBJS := $(OBJS:.java=.class)
ARCH := ppc
AS   := powerpc-eabi-as
LD   := powerpc-eabi-ld
RUN  := powerpc-eabi-run

#ARCH := ia32
#AS   := as
#LD   := ld

CLASSPATH := $(CLASSPATH):.:src/:src/ast/:src/tree/:src/scope/:src/visitors/:src/icode/:src/icode/instructions/:src/register/:src/icode/optimizer/:src/codegen/ia32/:src/codegen/ppc/:src/codegen/


all: mcc

.SUFFIXES: .java .class .e .s .o .icode
.PHONY: clean cleanexamples test test2 testall mcc ast
.PRECIOUS: $(EXAMPLES:.e=.icode) $(EXAMPLES:.e=.o) $(EXAMPLES:.e=.s) $(EXAMPLES:.e=.dot)


mcc: src/mcc.class

mcc-ant: src/Parser.java src/Yylex.java
	@ant

#EXAMPLE = examples/castings.e
#EXAMPLE = examples/io.e
EXAMPLE = examples/hallo_welt.e
test: $(EXAMPLE:.e=.icode) $(EXAMPLE:.e=.s) $(EXAMPLE:.e=)
	@#cat $(EXAMPLE:.e=.s)

test2: $(EXAMPLE)
	./mcc $(EXAMPLE)

ast: $(EXAMPLE:.e=.ps)
	@gv "$<"

testall: $(EXAMPLES:.e=)

src/mcc.class: src/mcc.java src/Parser.java src/Yylex.java $(OBJS)
	$(JAVAC) -classpath "$(CLASSPATH)" src/mcc.java
	@mkdir build/ 2> /dev/null || true
	@cp `find src/ -name '*.class'` build/

src/Parser.java: src/grammar.y
	$(BYACCJ) -Jsemantic=Object "$<"
	@mv Parser.java src/

src/Yylex.java: src/lexer.lex
	$(JAVA) -classpath "$(CLASSPATH)" $(JLEX) "$<"
	@mv src/lexer.lex.java src/Yylex.java

clean: cleanexamples
	@rm -f src/Parser.java src/Yylex.java
	@rm -f `find src/ -name '*.class'`
	@rm -rf build/

cleanexamples:
	@rm -f $(EXAMPLES:.e=) $(EXAMPLES:.e=.icode) $(EXAMPLES:.e=.o) $(EXAMPLES:.e=.s) $(EXAMPLES:.e=.dot) $(EXAMPLES:.e=.ps)

%.class: %.java
	$(JAVAC) -classpath "$(CLASSPATH)" "$<"

%: %.s
	$(AS) "$<" -o "$*.o"
	$(LD) -e start "$*.o" /usr/lib/libc.a -o "$@"

%.icode: %.e src/mcc.class
	./mcc -i "$@" "$<"

%.s: %.e src/mcc.class
	./mcc --arch $(ARCH) -S "$@" "$<"

%.dot: %.e src/mcc.class
	./mcc -a "$<" > "$@"

%.ps: %.dot
	dot -T ps "$<" > "$@"
