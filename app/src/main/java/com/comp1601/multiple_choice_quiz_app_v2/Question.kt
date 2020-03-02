package com.comp1601.multiple_choice_quiz_app_v2
import kotlin.random.Random

class Question (questionArray: Array<String>) {
    /**    Question array is expected to be of the form:
    - The first item is the actual question being asked
    - The second item is the CORRECT answer to the question
    - All following items are WRONG answers.
     */



    /**
     * Variables are attached to properties in Kotlin, not fields like in Java. Instead, Kotlin properties
     * are composed of a field (which stores the info), a getter and a setter (which are implicitly created)
     * NOTE: setters aren't created for immutable variables (with the keyword val)
     */
    val questionText:String = questionArray[0]
    val answer:String = questionArray[1]

    //for when the user clicks next or previous buttons, if unanswered, don't change any button's color, but if the button has already been answered (A, B, C, D , or E), preload the user's previous answer.
    var userAnswer = "UNANSWERED"
    var potentialAnswers:ArrayList<String> = ArrayList()


    /**
     * Initializes potential answers by filling a list of strings with potential answers IN ORDER
     */
    init{
        //fill the potential answers array with all possible answers from the question Array
        for (i in 1 until questionArray.size){
            potentialAnswers.add(questionArray[i])
        }
    }


    /**
     *  Shuffles the potential answers so that quizzes are unique
     */
    fun shuffleAnswers(){
        val listSize:Int = potentialAnswers.size

        for(i in 0 until listSize){
            //get a random number between the index and the size of the array
            val randomNumber = Random.nextInt(i, listSize)

            //sawp the elements of the current index and the randomly generated index
            val temp = potentialAnswers[i]
            potentialAnswers[i] = potentialAnswers[randomNumber]
            potentialAnswers[randomNumber] = temp
        }
    }




}
