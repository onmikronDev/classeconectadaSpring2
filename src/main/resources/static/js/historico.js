document.addEventListener("DOMContentLoaded", async () => {
  const materiasList = document.getElementById("materiasList");
  const notasList = document.getElementById("notasList");
  const pageTitle = document.querySelector("h1"); // Assumindo que há um h1 para título

  // ✅ CORRIGIDO: Obter studentId da URL
  const urlParams = new URLSearchParams(window.location.search);
  const studentId = urlParams.get('studentId');

  if (!studentId) {
    alert('ID do aluno não fornecido. Redirecionando...');
    window.location.href = 'turma.html';
    return;
  }

  // ✅ CORRIGIDO: Carregar dados do aluno e notas da API
  await carregarHistorico(studentId);

  /**
   * ✅ CORRIGIDO: Carregar histórico do aluno da API
   */
  async function carregarHistorico(studentId) {
    try {
      // Buscar dados do aluno
      const alunoResponse = await fetch(`http://localhost:8080/api/users/${studentId}`);
      if (!alunoResponse.ok) throw new Error('Erro ao carregar dados do aluno');
      const aluno = await alunoResponse.json();

      // ✅ CORRIGIDO: Atualizar título com nome do aluno
      if (pageTitle) {
        pageTitle.textContent = `Histórico de ${aluno.nome}`;
      }

      // Buscar notas do aluno
      const notasResponse = await fetch(`http://localhost:8080/api/grades/student/${studentId}`);
      if (!notasResponse.ok) throw new Error('Erro ao carregar notas');
      const notas = await notasResponse.json();

      // ✅ CORRIGIDO: Organizar notas por matéria
      const notasPorMateria = {};
      notas.forEach(nota => {
        const materiaNome = nota.subject.name;
        if (!notasPorMateria[materiaNome]) {
          notasPorMateria[materiaNome] = [];
        }
        notasPorMateria[materiaNome].push(nota);
      });

      // Preencher lista de matérias
      materiasList.innerHTML = "";
      Object.keys(notasPorMateria).forEach((materia) => {
        const li = document.createElement("li");
        li.textContent = materia;
        li.addEventListener("click", () => filtrarNotasPorMateria(materia, notasPorMateria));
        materiasList.appendChild(li);
      });

      // Inicialmente mostra todas as notas
      mostrarTodasNotas(notasPorMateria);

    } catch (error) {
      console.error('Erro ao carregar histórico:', error);
      alert('Erro ao carregar histórico. Verifique se o backend está rodando.');
    }
  }

  /**
   * Filtra as notas com base na matéria selecionada
   * @param {string} materia Nome da matéria
   * @param {Object} notasPorMateria Objeto com notas organizadas por matéria
   */
  function filtrarNotasPorMateria(materia, notasPorMateria) {
    notasList.innerHTML = ""; // Limpa as notas atuais
    notasPorMateria[materia].forEach((nota) => {
      const li = document.createElement("li");
      const dataFormatada = nota.examDate ? new Date(nota.examDate).toLocaleDateString('pt-BR') : 'N/A';
      li.textContent = `Nota: ${nota.value} - Data: ${dataFormatada}`;
      if (nota.description) {
        li.title = nota.description;
      }
      notasList.appendChild(li);
    });
  }

  /**
   * Mostra todas as notas agrupadas por matéria
   */
  function mostrarTodasNotas(notasPorMateria) {
    notasList.innerHTML = "";
    Object.entries(notasPorMateria).forEach(([materia, notas]) => {
      notas.forEach((nota) => {
        const li = document.createElement("li");
        const dataFormatada = nota.examDate ? new Date(nota.examDate).toLocaleDateString('pt-BR') : 'N/A';
        li.textContent = `${materia}: Nota ${nota.value} - Data: ${dataFormatada}`;
        if (nota.description) {
          li.title = nota.description;
        }
        notasList.appendChild(li);
      });
    });
  }

  // Botão Voltar
  document.getElementById("voltarBtn").addEventListener("click", () => {
    window.location.href = "turma.html";
  });
});
