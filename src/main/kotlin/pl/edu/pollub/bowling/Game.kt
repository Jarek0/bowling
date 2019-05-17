package pl.edu.pollub.bowling

import java.lang.IllegalArgumentException
import kotlin.math.min

class Game {

    private val scorer = Scorer()
    private var currentFrame = 0
    private var firstThrowInFrame = false

    fun addThrow(pins: Int) {
        scorer.addThrow(pins)
        adjustCurrentFrame(pins)
    }

    fun score() = scoreForFrame(currentFrame)

    fun scoreForFrame(frame: Int) = scorer.scoreForFrame(frame)

    private fun adjustCurrentFrame(pins: Int) {
        if(isLastBallInFrame(pins)) {
            advanceFrame()
        }
        else {
            firstThrowInFrame = false
        }
    }

    private fun isLastBallInFrame(pins: Int) = strike(pins) || !firstThrowInFrame

    private fun strike(pins: Int) = firstThrowInFrame && pins == 10

    private fun advanceFrame() {
        currentFrame = min(10, currentFrame + 1)
    }

}

class Scorer {

    private val throws = Array(21) { 0 }
    private var ball = 0
    private var currentThrow = 0

    fun addThrow(pins: Int) {
        if(pins > 10 || pins < 0) throw InvalidCountOfPins(pins)
        throws[currentThrow++] = pins
    }

    fun scoreForFrame(frame: Int): Int {
        ball = 0
        var score = 0
        for(currentFrame in 0 until frame) {
            when {
                isStrike() -> {
                    score += 10 + nextTwoBallsForStrike()
                    ball++
                }
                isSpare() -> {
                    score += 10 + nextBallForSpare()
                    ball += 2
                }
                else -> {
                    score += twoBallsInFrame()
                    ball += 2
                }
            }
        }
        return score
    }

    private fun isStrike() = throws[ball] == 10

    private fun isSpare() = (throws[ball] + throws[ball + 1]) == 10

    private fun nextTwoBallsForStrike() = throws[ball + 1] + throws[ball + 2]

    private fun nextBallForSpare() = throws[ball + 2]

    private fun twoBallsInFrame() = throws[ball] + throws[ball + 1]

}

class InvalidCountOfPins(val pins: Int): IllegalArgumentException("Invalid count of pins: $pins")