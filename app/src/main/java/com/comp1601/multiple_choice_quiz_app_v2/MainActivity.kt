package com.comp1601.multiple_choice_quiz_app_v2

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi

//NOTE: this extension allows us to access view objects without findViewByID
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var totalScore = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var currentQuestionIndex = 0

        //remove action bar at top
        supportActionBar?.hide()

        //create and fill array list of all questions
        val questions:ArrayList<Question> = ArrayList()
        questions.add(Question(resources.getStringArray(R.array.question1)))
        questions.add(Question(resources.getStringArray(R.array.question2)))
        questions.add(Question(resources.getStringArray(R.array.question3)))
        questions.add(Question(resources.getStringArray(R.array.question4)))
        questions.add(Question(resources.getStringArray(R.array.question5)))
        questions.add(Question(resources.getStringArray(R.array.question6)))
        questions.add(Question(resources.getStringArray(R.array.question7)))
        questions.add(Question(resources.getStringArray(R.array.question8)))
        questions.add(Question(resources.getStringArray(R.array.question9)))
        questions.add(Question(resources.getStringArray(R.array.question10)))

        //randomize answers for all questions
        for(i in 0 until questions.size){
            questions[i].shuffleAnswers()
        }

        //set question text and possible answers
        updateQuestionAnswersText(questions[currentQuestionIndex])
        //MULTIPLE CHOICE BUTTON LISTENERS:
        btnMultipleChoiceA.setOnClickListener{
            updateMCButton("A", questions[currentQuestionIndex])
        }
        btnMultipleChoiceB.setOnClickListener{
            updateMCButton("B", questions[currentQuestionIndex])
        }
        btnMultipleChoiceC.setOnClickListener{
            updateMCButton("C", questions[currentQuestionIndex])
        }
        btnMultipleChoiceD.setOnClickListener{
            updateMCButton("D", questions[currentQuestionIndex])
        }
        btnMultipleChoiceE.setOnClickListener{
            updateMCButton("E", questions[currentQuestionIndex])
        }


        //NEXT BUTTON LISTENER
        btnNext.setOnClickListener{
            //update current question index as long we're not on the last question
            val lastQuestionIndex = questions.size - 1
            if(currentQuestionIndex < lastQuestionIndex){

                //reset all button backgrounds
                val buttonBackgroundDefault = getDrawable(R.drawable.button_background)
                btnMultipleChoiceA.background = buttonBackgroundDefault
                btnMultipleChoiceB.background = buttonBackgroundDefault
                btnMultipleChoiceC.background = buttonBackgroundDefault
                btnMultipleChoiceD.background = buttonBackgroundDefault
                btnMultipleChoiceE.background = buttonBackgroundDefault

                //update which question we're on
                currentQuestionIndex++

                //change UI to reflect new question and possible answers
                updateQuestionAnswersText(questions[currentQuestionIndex])
            }
        }


        //PREVIOUS BUTTON LISTENER
        btnPrevious.setOnClickListener{
            //change current question index as long as we're not on the first question
            val firstQuestionIndex = 0
            if(currentQuestionIndex > firstQuestionIndex){

                //reset all button backgrounds
                val buttonBackgroundDefault = getDrawable(R.drawable.button_background)
                btnMultipleChoiceA.background = buttonBackgroundDefault
                btnMultipleChoiceB.background = buttonBackgroundDefault
                btnMultipleChoiceC.background = buttonBackgroundDefault
                btnMultipleChoiceD.background = buttonBackgroundDefault
                btnMultipleChoiceE.background = buttonBackgroundDefault

                //update which question we're on
                currentQuestionIndex--

                //change UI to reflect new question and possible answers
                updateQuestionAnswersText(questions[currentQuestionIndex])
            }
        }


        //SUBMIT BUTTON LISTENER
        btnSubmit.setOnClickListener{
            //Display user score
            Toast.makeText(this, "You got a score of: $totalScore/${questions.size}", Toast.LENGTH_LONG).show()

            //delete all previous question answers and reshuffle question answers
            for(question in questions){
                question.userAnswer = "UNANSWERED"
                question.shuffleAnswers()
            }

            //reset all button backgrounds
            val buttonBackgroundDefault = getDrawable(R.drawable.button_background)
            btnMultipleChoiceA.background = buttonBackgroundDefault
            btnMultipleChoiceB.background = buttonBackgroundDefault
            btnMultipleChoiceC.background = buttonBackgroundDefault
            btnMultipleChoiceD.background = buttonBackgroundDefault
            btnMultipleChoiceE.background = buttonBackgroundDefault

            //reset score
            totalScore = 0

            //reset question index
            currentQuestionIndex = 0

            //reset question text and possible answers
            updateQuestionAnswersText(questions[currentQuestionIndex])
        }
    }


    /**
     * completely updates a multiple choice button by:
     * - changing the score accordingly
     * - changing the background accordingly
     * - sets the user's chosen answer for the question object
     * @param currButtonLetter: the letter that the pressed button is associated with
     * @param question: the question the user is answering
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun updateMCButton(currButtonLetter:String, question:Question){
        val prevButtonLetter = question.userAnswer //represents which letter the user entered before (if it's a new question, this has a value of "UNANSWERED"

        //add to score if the user chose a different answer from before and that answer is correct
        if(prevButtonLetter != currButtonLetter && checkAnswer(currButtonLetter, question)){
            totalScore++
        }// subtract to score if: the user chose a different answer from before, the question is not unanswered, and the previous answer is correct
        else if(prevButtonLetter != "UNANSWERED" && prevButtonLetter != currButtonLetter && checkAnswer(prevButtonLetter, question))
            totalScore--

        //set the user's chosen answer
        question.userAnswer = currButtonLetter

        //map out the button's letter to its object
        val buttonObjMap:HashMap<String, Button> = HashMap()
        buttonObjMap["A"] = btnMultipleChoiceA
        buttonObjMap["B"] = btnMultipleChoiceB
        buttonObjMap["C"] = btnMultipleChoiceC
        buttonObjMap["D"] = btnMultipleChoiceD
        buttonObjMap["E"] = btnMultipleChoiceE

        //update button background
        val currButtonObject = buttonObjMap[currButtonLetter]
        if (currButtonObject != null) {
            updateButtonBackground(currButtonObject)
        }
    }



    /**
     * @param question: the question the user is on
     * updates the TextViews that display the question and its possible answers, resets button backgrounds
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun updateQuestionAnswersText(question:Question){

        val potentialAnswers = question.potentialAnswers
        val questionText = question.questionText
        var potentialAnswersText = ""
        val uppercaseASCIIAlphabet = 65 //represents where the first letter of the uppercase alphabet starts in the ASCII table (A)

        //format possible answers ex: "[A] PossibleAnswer"
        for(i in 0 until potentialAnswers.size){
            potentialAnswersText +="[" + (i+uppercaseASCIIAlphabet).toChar() + "] " +potentialAnswers[i] + "\n"
        }

        //update UI components
        txtAnswers.text = potentialAnswersText
        txtQuestion.text = questionText

        //change button background if the user already answered the question
        if(question.userAnswer != "UNANSWERED"){

            //map button letters to button widgets
            val buttonWidgetMap:HashMap<String, Button> = HashMap()
            buttonWidgetMap["A"] = btnMultipleChoiceA
            buttonWidgetMap["B"] = btnMultipleChoiceB
            buttonWidgetMap["C"] = btnMultipleChoiceC
            buttonWidgetMap["D"] = btnMultipleChoiceD
            buttonWidgetMap["E"] = btnMultipleChoiceE

            //load the user's previous button if they clicked one earlier
            val lastChosenButton = buttonWidgetMap[question.userAnswer]
            if (lastChosenButton != null) {
                updateButtonBackground(lastChosenButton)
            }
        }
    }




    /**
     * change the background of a clicked button and reset backgrounds of all other buttons
     * @param currentMultipleChoiceButton: the button widget that has been clicked
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun updateButtonBackground(currentMultipleChoiceButton: Button){

        //drawables for different button states
        val buttonBackgroundClicked = getDrawable(R.drawable.button_background_clicked)
        val buttonBackgroundDefault = getDrawable(R.drawable.button_background)

        //reset all button backgrounds
        btnMultipleChoiceA.background = buttonBackgroundDefault
        btnMultipleChoiceB.background = buttonBackgroundDefault
        btnMultipleChoiceC.background = buttonBackgroundDefault
        btnMultipleChoiceD.background = buttonBackgroundDefault
        btnMultipleChoiceE.background = buttonBackgroundDefault

        //change button background to clicked
        currentMultipleChoiceButton.background = buttonBackgroundClicked
    }

    /**
     * Checks if a given answer is correct or not
     * @param buttonLetter: the multiple choice button the user chose
     * @param question: the question that the user answered for
     * @return returns true if the answer associated with the multiple choice button matches the questions answer, false otherwise.
     */
    private fun checkAnswer(buttonLetter:String, question:Question):Boolean{
        //map button letters to their respective answers in the potentialAnswers Array List
        val buttonAnswerMap:HashMap<String, Int> = HashMap()
        buttonAnswerMap["A"] = 0
        buttonAnswerMap["B"] = 1
        buttonAnswerMap["C"] = 2
        buttonAnswerMap["D"] = 3
        buttonAnswerMap["E"] = 4


        val userAnswerIndex: Int = buttonAnswerMap[buttonLetter]!!
        val userAnswerText = question.potentialAnswers[userAnswerIndex] //represents the answer associated with user's letter choice


        if (userAnswerText == question.answer){
            return true
        }
        return false
    }

}
