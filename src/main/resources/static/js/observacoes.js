document.addEventListener("DOMContentLoaded", async () => {
  const materiasList = document.getElementById("materiasList");
  const notasList = document.getElementById("notasList");

  // ✅ CORRIGIDO: Obter studentId da URL (similar ao historico.js)
  const urlParams = new URLSearchParams(window.location.search);
  const studentId = urlParams.get('studentId');

  if (!studentId) {
    alert('ID do aluno não fornecido. Redirecionando...');
    window.location.href = 'turma.html';
    return;
  }

  // ✅ CORRIGIDO: Carregar observações da API
  await carregarObservacoes(studentId);

  /**
   * ✅ CORRIGIDO: Carregar observações do aluno da API
   */
  async function carregarObservacoes(studentId) {
    try {
      // Buscar dados do aluno
      const alunoResponse = await fetch(`http://localhost:8080/api/users/${studentId}`);
      if (!alunoResponse.ok) throw new Error('Erro ao carregar dados do aluno');
      const aluno = await alunoResponse.json();

      // Atualizar título com nome do aluno se existir
      const pageTitle = document.querySelector("h1");
      if (pageTitle) {
        pageTitle.textContent = `Observações de ${aluno.nome}`;
      }

      // Buscar observações do aluno
      const observacoesResponse = await fetch(`http://localhost:8080/api/observations/student/${studentId}`);
      if (!observacoesResponse.ok) throw new Error('Erro ao carregar observações');
      const observacoes = await observacoesResponse.json();

      // Preencher lista de observações
      notasList.innerHTML = "";
      if (observacoes.length === 0) {
        notasList.innerHTML = "<li>Nenhuma observação registrada</li>";
      } else {
        observacoes.forEach(obs => {
          const li = document.createElement("li");
          const dataFormatada = obs.date ? new Date(obs.date).toLocaleDateString('pt-BR') : 'N/A';
          li.textContent = `${dataFormatada}: ${obs.description || obs.text || 'Sem descrição'}`;
          li.addEventListener("click", () => selecionarObservacao(li, obs));
          notasList.appendChild(li);
        });
      }

    } catch (error) {
      console.error('Erro ao carregar observações:', error);
      alert('Erro ao carregar observações. Verifique se o backend está rodando.');
    }
  }

  let observacaoSelecionada = null;

  // Seleciona uma observação
  function selecionarObservacao(li, observacao) {
    if (observacaoSelecionada) observacaoSelecionada.classList.remove("selected");
    observacaoSelecionada = li;
    observacaoSelecionada.classList.add("selected");
    
    const texto = observacao.description || observacao.text || 'Sem descrição';
    const data = observacao.date ? new Date(observacao.date).toLocaleDateString('pt-BR') : 'N/A';
    alert(`Observação Selecionada:\nData: ${data}\nTexto: ${texto}`);
  }

  // Botão Editar
  document.getElementById("editarBtn").addEventListener("click", () => {
    if (observacaoSelecionada) {
      alert(`Editando observação:\n${observacaoSelecionada.textContent}`);
    } else {
      alert("Selecione uma observação para editar.");
    }
  });

  // Botão Deletar
  document.getElementById("deletarBtn").addEventListener("click", () => {
    if (observacaoSelecionada) {
      alert(`Deletando observação:\n${observacaoSelecionada.textContent}`);
      notasList.removeChild(observacaoSelecionada);
      observacaoSelecionada = null;
    } else {
      alert("Selecione uma observação para deletar.");
    }
  });

  // Botão Voltar
  document.getElementById("voltarBtn").addEventListener("click", () => {
    window.location.href = "turma.html";
  });
});11
