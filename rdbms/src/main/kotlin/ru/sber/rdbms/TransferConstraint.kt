package ru.sber.rdbms

import java.sql.DriverManager

class TransferConstraint {

        fun transfer(accountId1: Long, accountId2: Long, amount: Long) {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/db",
            "postgres",
            "postgres"
        )
        connection.use { conn ->
            val prepareStatement = conn.prepareStatement(
                "BEGIN;" +
                        "UPDATE accounts1 SET amount = amount + $amount WHERE id = $accountId2;\n" +
                        "UPDATE accounts1 SET amount = amount - $amount WHERE id = $accountId1;\n" +
                        "COMMIT;"
            )
            prepareStatement.execute()
        }
    }
}
