fun main() {
    println("=== Simple Calculator ===\n")
    
    val number1 = readNumber("Enter first number: ")
    val number2 = readNumber("Enter second number: ")
    val operator = readOperator()
    
    val result = calculate(number1, number2, operator)
    
    println("\nResult: $number1 $operator $number2 = $result")
}

fun readNumber(prompt: String): Double {
    while (true) {
        print(prompt)
        val number = readLine()?.toDoubleOrNull()
        
        if (number != null) return number
        println("Please enter a valid number.")
    }
}

fun readOperator(): String {
    val validOps = setOf("+", "-", "*", "/")
    
    while (true) {
        print("Enter operator (+, -, *, /): ")
        val operator = readLine()?.trim() ?: ""
        
        if (operator in validOps) return operator
        println("Invalid operator. Please use +, -, *, or /")
    }
}

fun calculate(a: Double, b: Double, operator: String): Double {
    return when (operator) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> {
            if (b == 0.0) {
                println("Warning: Division by zero, returning 0")
                0.0
            } else {
                a / b
            }
        }
        else -> error("Unexpected operator: $operator") 
    }
}