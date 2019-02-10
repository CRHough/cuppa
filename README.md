# cuppa

An example of calling C functions from Clojure via GraalVM.

## Usage

Uses [lein native-image](https://github.com/taylorwood/lein-native-image)

The included ```hello.c``` file needs to be compiled to llvm bitcode: ```clang -g -01 -c -emit-llvm hello.c```

You will need to set ```:graal-bin``` to your GraalVM installation location.

I also put the GraalVM bin directory and GraalVM jre/langauges/llvm directory on the path.

The GraalVM jre/languages/llvm directory also got added to the ```LD_LIBRARY_PATH```. Without that I got errors about not being able to find ```libsulong.so```. I also needed to symlink ```libsulong.bc``` and ```libsulong++.bc``` into the root project directory.

It's probable that none of the above is necessary but I don't know what I'm doing and these are the gyrations I went through to get it to work.

If, by some miracle, everything builds successfuly there should be an executable native image in target/. Running ```./target/cuppa``` is almost certain not to do anything horrible. If it does I take no responsibility at all.