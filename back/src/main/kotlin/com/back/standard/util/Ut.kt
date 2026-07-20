package com.back.standard.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import tools.jackson.databind.ObjectMapper
import java.util.*

object Ut {
    object jwt {
        @JvmStatic
        fun toString(secret: String, expireSeconds: Int, body: Map<String, Any>): String {
            val claimsBuilder = Jwts.claims()
            for ((key, value) in body) {
                claimsBuilder.add(key, value)
            }
            val claims = claimsBuilder.build()

            val issuedAt = Date()
            val expiration = Date(issuedAt.time + 1000L * expireSeconds)

            val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

            return Jwts.builder()
                .claims(claims)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact()
        }

        @JvmStatic
        fun isValid(secret: String, jwtStr: String): Boolean {
            return try {
                val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

                Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtStr)

                true
            } catch (e: Exception) {
                false
            }
        }

        @JvmStatic
        fun payload(secret: String, jwtStr: String): Map<String, Any>? {
            return try {
                val secretKey = Keys.hmacShaKeyFor(secret.toByteArray())

                LinkedHashMap(
                    Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwtStr)
                        .payload
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    object json {
        lateinit var objectMapper: ObjectMapper

        @JvmStatic
        @JvmOverloads
        fun toString(obj: Any, defaultValue: String? = null): String? {
            return try {
                objectMapper.writeValueAsString(obj)
            } catch (e: Exception) {
                defaultValue
            }
        }
    }

    object cmd {
        fun run(vararg args: String) {
            val isWindows = System.getProperty("os.name")
                .lowercase(Locale.getDefault())
                .contains("win")

            val builder = ProcessBuilder(
                args.map { it.replace("{{DOT_CMD}}", if (isWindows) ".cmd" else "") }
            )

            // 에러 스트림도 출력 스트림과 함께 병합
            builder.redirectErrorStream(true)

            // 프로세스 시작
            val process = builder.start()

            // 결과 출력
            process.inputStream.bufferedReader().useLines { lines ->
                lines.forEach { println(it) }
            }

            // 종료 코드 확인
            val exitCode = process.waitFor()
            println("종료 코드: $exitCode")
        }

        @JvmStatic
        fun runAsync(vararg args: String) {
            Thread(Runnable {
                run(*args)
            }).start()
        }
    }
}
