#Author: Anthony Grieco
#Date: 9-1-2023
#Purpose: This BASH Shell Script (Ubunutu Linux) determines whether files in the same directory as this program have a file type that corresponds to that of a Java (Compiler: javac), C++ (Compiler: g++), or C (Compiler: gcc) file. Every file of any of these types that this script finds in the given directory will be compiled and then the names of those files will be outputted to a file named "comp_out.txt" along with the compiler that was used to process the original file. Any error output will be placed in a file named "comp_err.txt".
#Important Note: All files being parsed by this script must be found within its same directory.

#Breaking Down the Problem:
# 1. For each item in the given directory (ls) check if it has a file extention of .java, .cpp, or .c

# 2. If it does, then compile it with the appropriate compiler
    

    #a. Output the name of the file, a comma, and then the name of the compiler used to compile the file to "comp_out.txt"
          #(ex: echo "[nameOfFile].[fileType], [compiler]" << comp_out.txt)
          
    #b. Output any errors (2<) that emerge when the given file is compiled to "comp_err.txt" 
          #(ex: [compiler] [nameOfFile].[fileType] 2< comp_err.txt)
          
# 3. Else, check the next item until there is nothing left in the list to check


touch comp_out.txt #A list of files that have extentions of either ".java", ".cpp", or ".c" and are compiled 
touch comp_err.txt #Errors related to a file not being able to compile gets put here

#The following two lines are used to automatically clear the files of all the information that they contained the previous session for the convenience of the user
> comp_out.txt
> comp_err.txt

for file in $(ls); do
  
  if [[ $file == *.java ]]; then
    echo "$file, javac" >> comp_out.txt
    javac $file 2>> comp_err.txt
    
  
  elif [[ $file == *.cpp ]]; then
    echo "$file, g++" >> comp_out.txt
    g++ -c $file 2>> comp_err.txt
    
    
  elif [[ $file == *.c ]]; then
    echo "$file, c" >> comp_out.txt
    gcc -c $file 2>> comp_err.txt
    
  fi

done