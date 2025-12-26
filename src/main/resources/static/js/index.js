document.addEventListener("DOMContentLoaded", async () => {
  const relatorioBtn = document.getElementById("relatorioBtn");
  const relatorioModal = document.getElementById("relatorioModal");
  const closeRelatorioModal = document.getElementById("closeRelatorioModal");
  const turmasList = document.getElementById("turmasList");
  const alunosList = document.getElementById("alunosList");
  const materiasList = document.getElementById("materiasList");
  const turmasContainer = document.getElementById("turmasContainer");
  const alunosContainer = document.getElementById("alunosContainer");
  const materiasContainer = document.getElementById("materiasContainer");

  // ✅ CORRIGIDO: Dados carregados das APIs
  let turmas = [];
  let alunosSelecionados = [];
  let notasDoAluno = [];

  relatorioBtn.addEventListener("click", async () => {
    relatorioModal.style.display = "flex";
    alunosContainer.style.display = "none";
    materiasContainer.style.display = "none";
    turmasContainer.style.display = "block";
    await carregarTurmas();
  });

  closeRelatorioModal.addEventListener("click", () => {
    relatorioModal.style.display = "none";
  });

  /**
   * ✅ CORRIGIDO: Carregar turmas da API
   */
  async function carregarTurmas() {
    try {
      const response = await fetch('http://localhost:8080/api/classes');
      if (!response.ok) throw new Error('Erro ao carregar turmas');
      
      turmas = await response.json();
      preencherTurmas();
    } catch (error) {
      console.error('Erro ao carregar turmas:', error);
      alert('Erro ao carregar turmas.');
    }
  }

  function preencherTurmas() {
    turmasList.innerHTML = "";
    turmas.forEach((turma) => {
      const li = document.createElement("li");
      li.textContent = turma.name;
      li.addEventListener("click", () => exibirAlunos(turma));
      turmasList.appendChild(li);
    });
  }

  /**
   * ✅ CORRIGIDO: Carregar alunos da turma selecionada
   */
  async function exibirAlunos(turma) {
    try {
      const response = await fetch(`http://localhost:8080/api/classes/${turma.id}/students`);
      if (!response.ok) throw new Error('Erro ao carregar alunos');
      
      alunosSelecionados = await response.json();
      
      materiasContainer.style.display = "none";
      alunosContainer.style.display = "block";
      turmasContainer.style.display = "none";
      
      alunosList.innerHTML = "";
      alunosSelecionados.forEach((aluno) => {
        const li = document.createElement("li");
        li.textContent = aluno.nome;
        li.addEventListener("click", () => exibirMaterias(aluno));
        alunosList.appendChild(li);
      });
    } catch (error) {
      console.error('Erro ao carregar alunos:', error);
      alert('Erro ao carregar alunos.');
    }
  }

  /**
   * ✅ CORRIGIDO: Carregar notas do aluno e calcular médias por matéria
   */
  async function exibirMaterias(aluno) {
    try {
      const response = await fetch(`http://localhost:8080/api/grades/student/${aluno.id}`);
      if (!response.ok) throw new Error('Erro ao carregar notas');
      
      notasDoAluno = await response.json();
      
      // ✅ CORRIGIDO: Organizar notas por matéria e calcular médias
      const notasPorMateria = {};
      notasDoAluno.forEach(nota => {
        const materiaNome = nota.subject.name;
        if (!notasPorMateria[materiaNome]) {
          notasPorMateria[materiaNome] = [];
        }
        notasPorMateria[materiaNome].push(nota.value);
      });

      materiasContainer.style.display = "block";
      alunosContainer.style.display = "none";
      
      materiasList.innerHTML = "";
      Object.entries(notasPorMateria).forEach(([materia, notas]) => {
        const media = calcularMedia(notas);
        const li = document.createElement("li");
        li.textContent = `${materia}: Média ${media}`;
        materiasList.appendChild(li);
      });

      // Se não houver notas
      if (Object.keys(notasPorMateria).length === 0) {
        materiasList.innerHTML = "<li>Nenhuma nota registrada</li>";
      }
    } catch (error) {
      console.error('Erro ao carregar notas:', error);
      alert('Erro ao carregar notas do aluno.');
    }
  }

  function calcularMedia(notas) {
    if (notas.length === 0) return "0.00";
    const soma = notas.reduce((total, nota) => total + nota, 0);
    return (soma / notas.length).toFixed(2);
  }
});
