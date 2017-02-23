This program was designed and built as the final project for our Natural Language Processing 
course at Williams College taught in Fall 2016 by visiting Professor Johan Boye. 

@authors: Braden Becker '19 and Bryan Bailey '19
@language: Java
@init_class: GUI.java

The program is run through the GUI.java file with the following flags determining project functioning:
java GUI -n <number> (2,3,4) -v <number> (2,3)

the -n flag will determine the n-gram level, and the v-flag will determine the viterbi decoding level
if no arguments are given, the program will initialize to a bigram model for both prediction and decoding


GUI usage:
-The large textfield is for user input only, the bottom one is used to display predictions

-The first word of each phrase (after program startup, period, or semicolon) will not display any predictions

-After predictions are available (wordcount > 0), each letter typed into the textfield should prune the list
       of possible predictions to only include those with the input stem (if any)

-In order to autocomplete a word to the most likely prediction displayed in bottom textfield,
     either with no letters typed or halfway through a word, press the *ENTER* key. You will notice
     that the letter saved variable updates your letters saved progress on the terminal window every time 
     you use a predicted word in your text

-A period and semicolon resets the word count; grams do not carry through this punctuation

-If a word is not contained in dictionary, which is checked after a *SPACE* key, the viterbi will attempt to decode automatically.
	Please note that the example given in our project paper of “heklo wodld” decoding to “hello world” was 
	done using bigram for the first word and trigram for the second word (not sure why they don’t decode similarly)

Known Bugs:
-Our program does not handle numbers and other strange punctuation other than alphabetical
	(null pointer exceptions from viterbi decoding), although prediction can continue 
	as normal after a word is input after number

-If a word is continuously decoded by viterbi for long enough (our limit is 100), it will decode to a much shorter word, which 
	is usually contained in our dictionary. This leads to strange behavior when incorrectly spelled long words decode
	to their short stems (e.g. “indybalew” —> “in”)

-DOES NOT HANDLE UPPERCASE in predictions at this time 


