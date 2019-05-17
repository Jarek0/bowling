package pl.edu.pollub.bowling

import spock.lang.Specification
import spock.lang.Subject

class GameSpec extends Specification {

    @Subject
    def game

    def setup() {
        game = new Game()
    }

    def "should calculate score for two throws in one frame with no mark"() {
        given:
            game.addThrow(4)
            game.addThrow(5)
        when:
            def score = game.score()
        then:
            score == 9
    }

    def "should calculate score for fours throws in two frames with no mark"() {
        given:
            game.addThrow(5)
            game.addThrow(4)

            game.addThrow(7)
            game.addThrow(2)
        when:
            def allFramesScore = game.score()
            def frame1Score = game.scoreForFrame(1)
            def frame2Score = game.scoreForFrame(2)
        then:
            allFramesScore == 18
            frame1Score == 9
            frame2Score == 18
    }

    def "should calculate score for frame with spare"() {
        given:
            game.addThrow(3)
            game.addThrow(7)

            game.addThrow(3)
        when:
            def score = game.scoreForFrame(1)
        then:
            score == 13
    }

    def "should calculate score for frame after spare"() {
        given:
            game.addThrow(3)
            game.addThrow(7)

            game.addThrow(3)
            game.addThrow(2)
        when:
            def frame1Score = game.scoreForFrame(1)
            def frame2Score = game.scoreForFrame(2)
            def allFramesScore = game.score()
        then:
            frame1Score == 13
            frame2Score == 18
            allFramesScore == 18
    }

    def "should calculate score for frame with strike"() {
        given:
            game.addThrow(10)
            game.addThrow(3)

            game.addThrow(6)
        when:
            def frame1Score = game.scoreForFrame(1)
            def allFramesScore = game.score()
        then:
            frame1Score == 19
            allFramesScore == 28
    }

    def "should calculate score for perfect game"() {
        given:
            for(i in 0..11) {
                game.addThrow(10)
            }
        when:
            def allFramesScore = game.score()
        then:
            allFramesScore == 300
    }

    def "should calculate score for strike in last throw"() {
        given:
            for(i in 0..8) {
                game.addThrow(0)
                game.addThrow(0)
            }
            game.addThrow(2)
            game.addThrow(8)
            game.addThrow(10)
        when:
            def allFramesScore = game.score()
        then:
            allFramesScore == 20
    }

    def "should calculate score for sample game"() {
        given:
            def points = [1, 4, 4, 5, 6, 4, 5, 5, 10, 0, 1, 7, 3, 6, 4, 10, 2, 8, 6]
            for(point in points) {
                game.addThrow(point)
            }
        when:
            def allFramesScore = game.score()
        then:
            allFramesScore == 133
    }

    def "should calculate score for almost perfect game"() {
        given:
            for(i in 0..10) {
                game.addThrow(10)
            }
            game.addThrow(9)
        when:
            def allFramesScore = game.score()
        then:
            allFramesScore == 299
    }

    def "should calculate score for game with spare in 10th frame"() {
        given:
            for(i in 0..8) {
                game.addThrow(10)
            }
            game.addThrow(9)
            game.addThrow(1)
            game.addThrow(1)
        when:
            def allFramesScore = game.score()
        then:
            allFramesScore == 270
    }

    def "should throw exception when points count for frame is invalid"() {
        when:
            game.addThrow(11)
        then:
            def exception = thrown(InvalidCountOfPins)
            exception.pins == 11
    }
}
