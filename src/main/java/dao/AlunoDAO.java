package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor; // Importar MongoCursor
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Aluno;
import org.bson.Document;
// import util.MongoConnection; // Removido se não for usado diretamente aqui

// Adicionar imports para List e ArrayList
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private MongoCollection<Document> docsAlunos;

    public AlunoDAO(mongoConexao connection){ // Usar MongoConexao corrigido
        if (connection != null && connection.getDatabase() != null) {
            this.docsAlunos = connection.getDatabase().getCollection("alunos"); //nome da colecao: alunos
        } else {
            // Idealmente, logar erro com SLF4J aqui também
            System.err.println("Falha ao inicializar AlunoDAO: conexão com o banco de dados não estabelecida.");
            // Lançar uma exceção pode ser apropriado aqui para sinalizar a falha
            throw new IllegalStateException("Não foi possível obter a coleção 'alunos' pois a conexão com o banco não está ativa.");
        }
    }

    public void initAlunos(){ // Este método pode ser usado para popular dados iniciais, se desejado
        if (docsAlunos.countDocuments() == 0) { // Insere apenas se a coleção estiver vazia
            Aluno nom1 = new Aluno("Igor Exemplo","508",55,43, 49); // Média calculada (55+43)/2 = 49
            docsAlunos.insertOne(nom1.toDocument());
            System.out.println("Aluno de exemplo inserido"); // Substituir por logger
        }
    }
//================================================CRUD================================================

    //CREATE
    public void createAluno (Aluno aluno){ //CRIA O ALUNO
        docsAlunos.insertOne(aluno.toDocument()); //INSERE NO BANCO DE DADOS
        // Idealmente, logar com SLF4J
        System.out.println("Aluno inserido: "+ aluno);
    }

    //READ - Modificado para retornar Lista de Alunos
    public List<Aluno> readAlunos(){
        List<Aluno> alunos = new ArrayList<>();
        // Idealmente, logar com SLF4J
        System.out.println("Lendo alunos cadastrados do banco...");
        try (MongoCursor<Document> cursor = docsAlunos.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                alunos.add(Aluno.fromDocument(doc));
            }
        }
        return alunos;
    }

    //UPDATE
    // O nome original é usado para encontrar o aluno, e os novos dados são aplicados
    public void updateAlunosFull(String nomeOriginal, String novoNome, String novaTurma, int novaNota1, int novaNota2, int novaMedia){
        // Se o nome (que é a chave de busca) pode ser alterado, a lógica de atualização é mais complexa.
        // Por simplicidade, vamos assumir que o 'nomeOriginal' é usado para encontrar o documento.
        // E 'novoNome' é o novo valor para o campo nome.
        Document updatesDoc = new Document()
                .append("nome", novoNome)
                .append("turma", novaTurma)
                .append("atividades", novaNota1)
                .append("provas", novaNota2)
                .append("media", novaMedia);

        docsAlunos.updateOne(Filters.eq("nome", nomeOriginal), new Document("$set", updatesDoc));
        // Idealmente, logar com SLF4J
        System.out.println("Aluno " + nomeOriginal + " atualizado para " + novoNome);
    }

    //DELETE
    public void deleteAluno(String nome){
        docsAlunos.deleteOne(Filters.eq("nome", nome));
        // Idealmente, logar com SLF4J
        System.out.println("Aluno " + nome + " apagado");
    }
}