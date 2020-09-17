import net.thecodersbreakfast.lp4j.midi.*
import net.thecodersbreakfast.lp4j.api.*

/**
 * Extremely standard snake game. No scoring.
 */
public class Snake(var refresh: Long, var launchpad: Launchpad) : LaunchpadListener {
    
    // grid configuration    
    val rows = 8
    val cols = 8
    var isGameOver = false
    var isToReset = true 

    
    var inputBuffer = ArrayList<Direction>() // stores all the inputs entered by the user
    var grid = Array(rows, {_ -> Array(cols, {_ -> 0})}) 
    var client = launchpad.client 

    var snake = ArrayList<Pair<Int,Int>>() 
    var snakeDirection = Direction.UP
    var candy: Pair<Int,Int> = Pair(0,0)    
    
    val colors: Array<Color> = arrayOf(Color.ORANGE,Color.RED,Color.YELLOW,Color.GREEN,Color.AMBER)
    var colorIdx = 0;
    var currentColor = Color.RED

    enum class Direction {
        UP,DOWN,RIGHT,LEFT
    }

    fun play() {
        launchpad.setListener(this)

        while(true) { // main loop, must be killed manually
            Thread.sleep(refresh)

            if (isToReset)
                reset()

            while(!isGameOver) {
                draw() // refresh the pad with new information
                
                val head = snake[0] 

                if (!inputBuffer.isEmpty()){ 
                    snakeDirection = inputBuffer.removeAt(0)
                }

                Thread.sleep(refresh)
                
                var newHead = when (snakeDirection) {
                    Direction.UP -> Pair(head.first, head.second-1)
                    Direction.DOWN -> Pair(head.first, head.second+1)
                    Direction.RIGHT -> Pair(head.first+1, head.second)
                    Direction.LEFT -> Pair(head.first-1, head.second)
                }
    
                if (hit(newHead)) {
                    isGameOver = true
                    client.testLights(LightIntensity.HIGH) // wipe the screen out. This could be improved with a game over screen
                } else {
                    // moving the snake
                    for (ring in snake.size-1 downTo 1) {
                        snake[ring] = snake[ring-1] // shift every ring towards the previous one
                    }    
                    snake.set(0, newHead)
                              
                    // check if contact with candy
                    if (snake.get(0) == candy) {
                        snake.add(Pair(snake.last().first, snake.last().second)) 
                        candy = Pair((0..7).random(), (0..7).random())
                        nextColor()
                    }     
                }
            }            
        }
    }

    /**
     * Draw the grid 
     */
    fun draw() {
        client.reset()
        for (p in snake) {
            client.setPadLight(Pad.at(p.first, p.second), currentColor, BackBufferOperation.NONE)
        }
        
        client.setPadLight(Pad.at(candy.first, candy.second), Color.GREEN, BackBufferOperation.NONE)
    }

    /**
     * Iterate over the color array and pick the next one
     */
    fun nextColor() : Color {
        currentColor = colors[colorIdx]
        colorIdx ++
        if (colorIdx >= colors.size)
            colorIdx = 0

        return currentColor
    }

    /**
     * Checks if the snake's head collided with a wall, or itself
     */
    fun hit(head: Pair<Int,Int>) : Boolean {
        if (head.first < 0 || head.second < 0 || head.first >= cols || head.second >= rows) {
            return true
        }
        
        snake.forEach {
            if (it == head)
                return true
        }

        return false
    }

    fun reset() {
        candy = Pair((0..7).random(), (0..7).random())
        snake = ArrayList<Pair<Int,Int>>()
        inputBuffer = ArrayList<Direction>()
        snake.add(Pair(4,4));
        snake.add(Pair(4,4));
        snake.add(Pair(4,4));
        isGameOver = false
        isToReset = false
    }

    override fun onButtonPressed(button: Button,timestamp: Long) {
        when(button) {
            Button.UP -> inputBuffer.add(Direction.UP)
            Button.DOWN -> inputBuffer.add(Direction.DOWN)
            Button.RIGHT -> inputBuffer.add(Direction.RIGHT)
            Button.LEFT -> inputBuffer.add(Direction.LEFT)
            Button.STOP -> isGameOver = true
            Button.SESSION -> isToReset = true
            else -> {}
        }
    }

    override fun onButtonReleased(button: Button,timestamp: Long) {

    }
    override fun onTextScrolled(timestamp: Long) {

    }
    override fun onPadPressed(pad: Pad,timestamp: Long) {

    }
    override fun onPadReleased(pad: Pad,timestamp: Long) {

    }    
}   