package dao;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException; // Import for specific MongoDB exceptions

public class mongoConexao {

    // A URI de conexão para o MongoDB local.
    // O nome do banco de dados "alunospecs" pode ser incluído aqui ou definido separadamente.
    // Se o banco de dados não existir, ele será criado na primeira operação de escrita.
    public static final String USERNAME = "igorcquintino"; //USUARIO
    public static final String PASSWORD  = "gtn8MzoptGhZRR58"; //SENHA
    public static final String CLUSTER_URL =  "cluster0poo.6esceum.mongodb.net"; //CLUSTER
    public static final String DATABASE_NAME = "AlunosSpecs"; //NOMNE DO BANCO DE DADOS
    private static final String CONNECTION_URI = "mongodb+srv://"+ USERNAME+":"+PASSWORD+"@"+CLUSTER_URL+"/?retryWrites=true&www=majority&appName="+DATABASE_NAME;

    // Opcional: definir o nome do banco de dados separadamente se não estiver na URI
    // ou se você quiser flexibilidade para mudá-lo.
    // Se você incluir o nome do banco na URI, esta constante pode não ser estritamente necessária
    // para a obtenção do banco de dados, mas é bom tê-la para clareza.
//    public static final String DATABASE_NAME = "Cluster0POO";

    private MongoClient mongoClient;
    private MongoDatabase database;

    public mongoConexao() {
        try {
            System.out.println("Tentando conectar com: " + CONNECTION_URI);

            // Configura a string de conexão
            ConnectionString connString = new ConnectionString(CONNECTION_URI);

            // Configura as definições do cliente MongoDB a partir da string de conexão
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    // Você pode adicionar outras configurações aqui se necessário, como timeouts:
                    // .applyToClusterSettings(builder ->
                    //         builder.serverSelectionTimeout(5000, TimeUnit.MILLISECONDS))
                    .build();

            // Cria o cliente MongoDB
            this.mongoClient = MongoClients.create(settings);

            // Obtém o banco de dados.
            // Se o nome do banco de dados já estiver na CONNECTION_URI,
            // o getDatabase() sem argumento também funcionaria se a URI fosse
            // "mongodb://localhost:27017/" e você usasse client.getDatabase("alunospecs").
            // Mas como especificamos "/alunospecs" na URI, ele já se conecta a esse banco por padrão.
            // Usar DATABASE_NAME aqui garante que estamos pegando o banco de dados correto.
            this.database = mongoClient.getDatabase(DATABASE_NAME);

            // Testa a conexão (opcional, mas recomendado)
            // Tenta executar um comando simples, como ping, para verificar a conexão.
            database.runCommand(new org.bson.Document("ping", 1));

            System.out.println("Conexão com MongoDB (" + CONNECTION_URI + ") bem-sucedida!");
            System.out.println("Banco de dados selecionado: " + database.getName());

        } catch (MongoException e) { // Captura exceções específicas do MongoDB
            System.err.println("Erro de conexão com MongoDB: " + e.getMessage());
            e.printStackTrace();
            // Você pode querer lançar uma RuntimeException ou tratar de outra forma
            // para sinalizar que a aplicação não pode continuar sem o banco.
            // throw new RuntimeException("Não foi possível conectar ao MongoDB", e);
        } catch (Exception e) { // Captura outras exceções gerais
            System.err.println("Ocorreu um erro inesperado durante a conexão com MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public MongoDatabase getDatabase() {
        if (this.database == null) {
            System.err.println("A conexão com o banco de dados não foi estabelecida ou falhou.");
            // Lançar uma exceção ou retornar null pode ser apropriado aqui,
            // dependendo de como o resto da sua aplicação lida com isso.
            throw new IllegalStateException("A instância do banco de dados não está disponível. Verifique os logs de conexão.");
        }
        return this.database;
    }

    // Método para fechar a conexão com o MongoDB
    public void close() {
        if (this.mongoClient != null) {
            try {
                this.mongoClient.close();
                System.out.println("Conexão com MongoDB fechada.");
            } catch (Exception e) {
                System.err.println("Erro ao fechar a conexão com MongoDB: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Método main para teste rápido da conexão (opcional)
    public static void main(String[] args) {
        mongoConexao conexao = null;
        try {
            conexao = new mongoConexao();
            MongoDatabase db = conexao.getDatabase();
            if (db != null) {
                System.out.println("Teste de obtenção do banco de dados bem-sucedido: " + db.getName());
                // Você poderia tentar listar coleções ou realizar outra operação simples aqui
                System.out.println("Coleções no banco de dados:");
                for (String name : db.listCollectionNames()) {
                    System.out.println("- " + name);
                }
            }
        } catch (Exception e) {
            System.err.println("Falha no teste de conexão: " + e.getMessage());
        } finally {
            if (conexao != null) {
                conexao.close();
            }
        }
    }
}
