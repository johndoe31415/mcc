Severin Strobl - sisestro - 2105031
Johannes Bauer - sijsbaue - 21089050
Tobias Preclik - sitoprec - 2104518

Quick Start
-----------

Compiletime Dependencies: JLex, BYacc/J, Java 5 compatible VM
Runtime Dependencies:     Java 5 compatible VM

If BYacc/J, JLex and or the Java VM are not ordinarily installed you need to
set some pathes. Therefore copy the config.template to config and edit it.
Adapt the necessary paths and source the config file into your current
environment.

	cp config.template config
	<your favorite editor> config
	. config


Compile the project.
	
	make


After a successful compilation you can type for example
	./mcc -a examples/fak.e | dot -Tps -o fak.ps
	./mcc --arch ppc -S fak.s examples/fak.e
	./as -o fak.o fak.s && ./ld -o fak fak.o /usr/lib/libc.a
	./fak
	make examples/ttt && examples/ttt
	make testall
	cd examples/ && ./regressiontest

Other command line parameters:
	-a			Show AST
	-s			Show variable scopes
	-i <file>	Generate icode and write to file
	-S <file>	Generate architecture depended assembler code to file
	--arch [ia32|ppc]	Select architecture to generate code for


Installation Instructions
-------------------------

In order to compile this project you need to install JLex and BYacc/J which
are available from:

	http://www.cs.princeton.edu/~appel/modern/java/JLex/
	http://byaccj.sourceforge.net/

Compile the JLex source in a directory named "JLex" (which is important!) and
add the parent directory to the java classpath:

	export CLASSPATH=$CLASSPATH:<jlex parent directory>

Compile the BYacc/J source and install it. If you haven't got the necessary
rights you can place the byaccj in any directory which is part of the PATH
environment variable or add the path to the byaccj binary to the PATH:

	export PATH=<byaccj directory>:${PATH}

Make sure you have a recent java virtual machine which supports java 5 code. If
you have several java vms installed and need to select the proper one you need
to set several environment variables. For Sun's Java development kit this would
be:
	
	export JDK_HOME=<jdk directory>
	export JAVA_HOME=${JDK_HOME}
	export JAVAC=${JDK_HOME}/bin/javac
	export PATH=${JDK_HOME}/bin:${JDK_HOME}/jre/bin:${PATH}
	export MANPATH=${MANPATH}:${JDK_HOME}/man

If you setup everything correctly you can go now into the project's src/
subdirectory and compile the project:

	cd src/
	make

After a successful compilation you can type for example
	mcc -a ../examples/fak.e | dot -Tps -o fak.ps
