package com.example.pson.smarttest.ui.game

//số câu trả lời tối đa trong 1 trò chơi
const val MAX_NUMBER_OF_QUESTION = 10

//list giá trị của toán hạng
val operantsValue: List<Int> = (1..100).toList().shuffled()

//các loại câu hỏi
val questionTypes = listOf("+", "-", "*", "/", "%")
