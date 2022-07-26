package com.example.pson.smarttest.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.example.pson.smarttest.database.ScoreboardDao
import com.example.pson.smarttest.database.ScoreboardItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(private val scoreboardDao: ScoreboardDao) : ViewModel() {

    /**
        Game Fragment layout variables & methods section
     */
    //Điểm đã đạt được
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    //Số câu đã trả lời
    private val _noQuestAnswered = MutableLiveData<Int>()
    val noQuestAnswered: LiveData<Int> get() = _noQuestAnswered

    //Câu hỏi hiện tại
    private val _currentQuestion = MutableLiveData<String>()
    val currentQuestion: LiveData<String> get() = _currentQuestion

    //2 biến cho câu hỏi hiện tại
    private var operant1: Int? = null
    private var operant2: Int? = null

    //kết quả của câu hỏi hiện tại
    private var result: Int? = null

    //Các câu trả lời hiện tại
    private val _buttonA = MutableLiveData<Int>()
    val buttonA: LiveData<Int> get() = _buttonA
    private val _buttonB = MutableLiveData<Int>()
    val buttonB: LiveData<Int> get() = _buttonB
    private val _buttonC = MutableLiveData<Int>()
    val buttonC: LiveData<Int> get() = _buttonC
    private val _buttonD = MutableLiveData<Int>()
    val buttonD: LiveData<Int> get() = _buttonD

    //khởi tạo instant bộ đếm
    private var timer: CountDownTimer = Timer()
    private val _remainTime = MutableLiveData<Long>()
    val remainTime: LiveData<Long> get() = _remainTime

    //khởi tạo lại trò chơi
    fun reinitializeGame() {
        _score.value = 0
        _noQuestAnswered.value = 0
        getNextQuestion()
        timer.start()
    }

    //tạo 1 câu hỏi ngẫu nhiên
    private fun getRandomQuestion() {
        //quay random
        operant1 = operantsValue.random()
        operant2 = operantsValue.random()
        while (operant1!! < operant2!!) operant2 = operantsValue.random()
        val operator = questionTypes.random()
        if (operator != "/")
            _currentQuestion.value = "$operant1 $operator $operant2 = ?"
        else
            _currentQuestion.value = "Floor[$operant1 $operator $operant2]="
        //tính kết quả của câu random trên
        result = when (operator) {
            "+" -> operant1!! + operant2!!
            "-" -> operant1!! - operant2!!
            "*" -> operant1!! * operant2!!
            "/" -> operant1!! / operant2!!
            else -> operant1!! % operant2!!
        }
    }

    fun increaseScore() {
        _score.value = (_score.value)?.plus(scoreIncrease!!)
    }

    //tạo 4 lựa chọn cho mỗi câu hỏi đã được tạo, trong đó có 1 lựa chọn đúng
    private fun getRandomSelections() {
        //tạo 4 đáp án rối dựa theo result trong 1 mảng
        val answers: MutableList<Int> = mutableListOf(result!!) //khởi tạo rỗng
        repeat(3) {
            val error = result!! + (1..10).random()
            answers.add(error)
        }
        //đảo tung mảng answers rồi apply lần lượt cho 4 button
        answers.shuffle()
        _buttonA.value = answers[0]
        _buttonB.value = answers[1]
        _buttonC.value = answers[2]
        _buttonD.value = answers[3]
    }

    //kiểm tra nếu đã trả lời 10 / 10 câu
    fun nextWord(): Boolean {
        return _noQuestAnswered.value!! < MAX_NUMBER_OF_QUESTION
    }

    //kiểm tra liệu câu trả lời có đúng
    fun isUserAnswerCorrect(userAnswer: String): Boolean {
        return userAnswer == result.toString()
    }

    //khởi tạo câu hỏi và các đáp án tiếp theo
    //đồng thời tạo bộ đếm
    fun getNextQuestion() {
        getRandomQuestion()
        getRandomSelections()
        //tăng số câu hỏi đã trả lời
        _noQuestAnswered.value = (_noQuestAnswered.value)?.inc()
        timer.start()
    }

    /**
        Timer config section
     */

    private var scoreIncrease: Int? = null

    //Bộ đếm thời gian (tổng 5s, giảm 1s)
    inner class Timer : CountDownTimer(8000, 1000) {
        override fun onTick(p0: Long) {
            _remainTime.value = p0 / 1000
            scoreIncrease = (p0/100).toInt()
        }

        override fun onFinish() {
            _remainTime.value = 0 //reset
            if (nextWord())
                getNextQuestion()
        }
    }

    //đóng băng thời gian
    fun freezeTime() = timer.cancel()

    /**
        Database method section
    */
    //get section
    val topResults : LiveData<List<ScoreboardItem>> = scoreboardDao.getTopResults().asLiveData()

    //insert section
    private suspend fun insertItem(scoreboardItem: ScoreboardItem) {
        scoreboardDao.insert(scoreboardItem)
    }

    private fun getScoreboardItemEntry(
        playerName: String,
        playerScore: String,
        playerTime: String
    ): ScoreboardItem {
        return ScoreboardItem(
            name = playerName,
            score = playerScore,
            time = playerTime
        )
    }

    private suspend fun addNewScoreboardItem(
        playerName: String,
        playerScore: String,
        playerTime: String
    ) {
        val newItem = getScoreboardItemEntry(playerName, playerScore, playerTime)
        insertItem(newItem)
    }

    //update section
    private suspend fun update(playerName: String, playerScore: String, playerTime: String) {
        scoreboardDao.update(playerName, playerScore, playerTime)
    }

    fun updateHigherScore(playerName: String, playerScore: String, playerTime: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isOldPlayer(playerName)) {
                val oldPlayer = scoreboardDao.getPlayerResultWithName(playerName)!!
                if (oldPlayer.score.toInt() < _score.value!!) {
                    update(playerName, playerScore, playerTime)
                }
            } else {
                addNewScoreboardItem(playerName, playerScore, playerTime)
            }
        }
    }

    private suspend fun isOldPlayer(playerName: String) : Boolean {
        return scoreboardDao.getPlayerResultWithName(playerName) != null
    }
}

class GameViewModelFactory(private val scoreboardDao: ScoreboardDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(scoreboardDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}