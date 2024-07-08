package com.example.jogodavelha

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.jogodavelha.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Vetor bidimensional que representará o tabuleiro de jogo
    val tabuleiro = arrayOf(
        arrayOf("", "", ""),
        arrayOf("", "", ""),
        arrayOf("", "", "")
    )

    // Qual o jogador está jogando
    var jogadorAtual = "bota"
    var dificuldade = "Fácil" // Adiciona a variável de dificuldade
    var contraMaquina = false // Adiciona a variável para determinar se está jogando contra a máquina

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Adiciona a seleção de dificuldade (pode ser feita via UI)
        findViewById<Button>(R.id.dificuldadeFacil).setOnClickListener {
            dificuldade = "Fácil"
            Toast.makeText(this, "Dificuldade: Fácil", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.dificuldadeDificil).setOnClickListener {
            dificuldade = "Difícil"
            Toast.makeText(this, "Dificuldade: Difícil", Toast.LENGTH_SHORT).show()
        }

        // Adiciona a seleção do modo de jogo
        findViewById<Button>(R.id.modoJogadorVsMaquina).setOnClickListener {
            contraMaquina = true
            Toast.makeText(this, "Modo de jogo: Contra a Máquina", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.modoJogadorVsJogador).setOnClickListener {
            contraMaquina = false
            Toast.makeText(this, "Modo de jogo: Contra um Humano", Toast.LENGTH_SHORT).show()
        }
    }

    // Função associada com todos os botões @param view é o botão clicado
    fun buttonClick(view: View) {
        // O botão clicado é associado com uma constante
        val buttonSelecionado = view as Button

        // Define o drawable baseado no jogador atual
        val drawableResource = if (jogadorAtual == "bota") R.drawable.bota else R.drawable.fla
        val drawable: Drawable? = getDrawable(drawableResource)
        buttonSelecionado.setBackground(drawable)

        // De acordo com o botão clicado, a posição da matriz receberá o jogador
        when (buttonSelecionado.id) {
            R.id.buttonZero -> tabuleiro[0][0] = jogadorAtual
            R.id.buttonUm -> tabuleiro[0][1] = jogadorAtual
            R.id.buttonDois -> tabuleiro[0][2] = jogadorAtual
            R.id.buttonTres -> tabuleiro[1][0] = jogadorAtual
            R.id.buttonQuatro -> tabuleiro[1][1] = jogadorAtual
            R.id.buttonCinco -> tabuleiro[1][2] = jogadorAtual
            R.id.buttonSeis -> tabuleiro[2][0] = jogadorAtual
            R.id.buttonSete -> tabuleiro[2][1] = jogadorAtual
            R.id.buttonOito -> tabuleiro[2][2] = jogadorAtual
        }

        // Verifica o vencedor
        val vencedor = verificaVencedor(tabuleiro)

        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Alterna jogador
            jogadorAtual = if (jogadorAtual == "bota") "fla" else "bota"

            // Desabilita o botão selecionado
            buttonSelecionado.isEnabled = false

            // Se for a vez da máquina jogar e o modo de jogo for contra a máquina, faz a jogada da máquina
            if (contraMaquina && jogadorAtual == "fla") {
                Handler(Looper.getMainLooper()).postDelayed({
                    jogadaMaquina()
                }, 1000) // Adiciona um delay de 1 segundo
            }
        }
    }

    private fun jogadaMaquina() {
        if (dificuldade == "Fácil") {
            jogadaFacil()
        } else {
            jogadaDificil()
        }
    }

    private fun jogadaFacil() {
        // Procura uma célula vazia para a jogada da máquina
        val movimentosPossiveis = mutableListOf<Pair<Int, Int>>()
        for (i in 0..2) {
            for (j in 0..2) {
                if (tabuleiro[i][j].isEmpty()) {
                    movimentosPossiveis.add(Pair(i, j))
                }
            }
        }

        // Seleciona um movimento aleatório
        if (movimentosPossiveis.isNotEmpty()) {
            val movimento = movimentosPossiveis[Random.nextInt(movimentosPossiveis.size)]
            tabuleiro[movimento.first][movimento.second] = jogadorAtual

            // Atualiza o texto do botão correspondente
            when {
                movimento.first == 0 && movimento.second == 0 -> findViewById<Button>(R.id.buttonZero).performClick()
                movimento.first == 0 && movimento.second == 1 -> findViewById<Button>(R.id.buttonUm).performClick()
                movimento.first == 0 && movimento.second == 2 -> findViewById<Button>(R.id.buttonDois).performClick()
                movimento.first == 1 && movimento.second == 0 -> findViewById<Button>(R.id.buttonTres).performClick()
                movimento.first == 1 && movimento.second == 1 -> findViewById<Button>(R.id.buttonQuatro).performClick()
                movimento.first == 1 && movimento.second == 2 -> findViewById<Button>(R.id.buttonCinco).performClick()
                movimento.first == 2 && movimento.second == 0 -> findViewById<Button>(R.id.buttonSeis).performClick()
                movimento.first == 2 && movimento.second == 1 -> findViewById<Button>(R.id.buttonSete).performClick()
                movimento.first == 2 && movimento.second == 2 -> findViewById<Button>(R.id.buttonOito).performClick()
            }
        }
    }

    private fun jogadaDificil() {
        // Implementa uma jogada mais inteligente para a máquina (exemplo simples)
        // Tenta ganhar ou bloquear o jogador
        val movimento = melhorMovimento()
        if (movimento != null) {
            tabuleiro[movimento.first][movimento.second] = jogadorAtual

            // Atualiza o texto do botão correspondente
            when {
                movimento.first == 0 && movimento.second == 0 -> findViewById<Button>(R.id.buttonZero).performClick()
                movimento.first == 0 && movimento.second == 1 -> findViewById<Button>(R.id.buttonUm).performClick()
                movimento.first == 0 && movimento.second == 2 -> findViewById<Button>(R.id.buttonDois).performClick()
                movimento.first == 1 && movimento.second == 0 -> findViewById<Button>(R.id.buttonTres).performClick()
                movimento.first == 1 && movimento.second == 1 -> findViewById<Button>(R.id.buttonQuatro).performClick()
                movimento.first == 1 && movimento.second == 2 -> findViewById<Button>(R.id.buttonCinco).performClick()
                movimento.first == 2 && movimento.second == 0 -> findViewById<Button>(R.id.buttonSeis).performClick()
                movimento.first == 2 && movimento.second == 1 -> findViewById<Button>(R.id.buttonSete).performClick()
                movimento.first == 2 && movimento.second == 2 -> findViewById<Button>(R.id.buttonOito).performClick()
            }
        } else {
            // Caso não haja movimentos vencedores ou bloqueios, faz uma jogada fácil
            jogadaFacil()
        }
    }

    private fun melhorMovimento(): Pair<Int, Int>? {
        // Procura uma jogada vencedora ou bloqueio
        for (i in 0..2) {
            for (j in 0..2) {
                if (tabuleiro[i][j].isEmpty()) {
                    // Tenta ganhar
                    tabuleiro[i][j] = jogadorAtual
                    if (verificaVencedor(tabuleiro) == jogadorAtual) {
                        tabuleiro[i][j] = ""
                        return Pair(i, j)
                    }
                    tabuleiro[i][j] = ""

                    // Tenta bloquear o adversário
                    tabuleiro[i][j] = if (jogadorAtual == "bota") "fla" else "bota"
                    if (verificaVencedor(tabuleiro) == if (jogadorAtual == "bota") "fla" else "bota") {
                        tabuleiro[i][j] = ""
                        return Pair(i, j)
                    }
                    tabuleiro[i][j] = ""
                }
            }
        }
            return null
    }

    fun verificaVencedor(tabuleiro: Array<Array<String>>): String? {
        // Verifica linhas e colunas
        for (i in 0 until 3) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0].isNotEmpty()) {
                return tabuleiro[i][0]
            }
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i] && tabuleiro[0][i].isNotEmpty()) {
                return tabuleiro[0][i]
            }
        }
        // Verifica diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0].isNotEmpty()) {
            return tabuleiro[0][0]
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2].isNotEmpty()) {
            return tabuleiro[0][2]
        }

        // Verifica empate
        var empate = 0
        for (linha in tabuleiro) {
            for (valor in linha) {
                if (valor == "bota" || valor == "fla") {
                    empate++
                }
            }
        }

        if (empate == 9) {
            return "Empate"
        }

        // Nenhum vencedor
        return null
    }
}
