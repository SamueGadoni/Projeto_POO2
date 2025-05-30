package model;

import org.bson.Document;

//construtores

public class Aluno {
    private String turma;
    private String nome;
    private int nota1;
    private int nota2;
    private int media;
    public Aluno(){

    }
    public Aluno(String nome,String turma,int nota1,int nota2, int media){
        this.turma = turma;
        this.nome = nome;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.media = media;
    }
    //NOEM
    public String getNome(){ return nome;}

    public void setNome(String nome){this.nome = nome;}

    //TURMA
    public String getTurma(){ return turma;}

    public void setTurma(String turma){this.turma = turma;}

    //NOTA1
    public int getNota1(){ return nota1;}

    public void setNota1(Integer nota1){this.nota1 = nota1;}

    //NOTA2
    public int getNota2(){ return nota2;}

    public void setNota2(Integer nota2){this.nota2 = nota2;}

    //MEDIA
    public int getMedia(){ return media;}

    public void setMedia(Integer media){this.media = media;}

    //inserindo a tabela mongodb
    public Document toDocument(){
        return new Document("nome",nome)
                .append("turma",turma)
                .append("atividades",nota1)
                .append("provas",nota2)
                .append("media",media);
    }
    public static Aluno fromDocument(Document document){
        return new Aluno(
                document.getString("nome"),
                document.getString("turma"),
                document.getInteger("atividades"),
                document.getInteger("provas"),
                document.getInteger("media"));
    }

    @Override
    public String toString(){
        return "Aluno{"
                +"nome = " +nome +'\''
                +"turma='"+turma+'\''
                +", atividades='" + nota1+'\''
                + "provas='"+nota2+'\''
                +"media='"+media+'\''+"}";
    }
}
