package com.example.goku.using.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by Goku on 30/06/2017.
 */

public class Converter {
    //Este método é responsável por converter o objeto recebido em uma string
    public static String toString(InputStream in, String charset){

        try {
            //Criando e instanciando um objeto que está recebedo os dados de um método chamado(método criado abaixo deste 'toBytes')
            byte[] bytes = toBytes(in);
            //Colocando os dados convertidos em bytes na string texto, 'charset' são tipos de caractéres, exemplo "UTF-8"
            String texto = new String(bytes, charset);
            //Retorna a String com o contéudo já convertido
            return texto;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Método responsável por converter os dados recebidos da webservice em bytes
    public static byte[] toBytes(InputStream in){
        //Instanciando um objeto que receberá os bytes e os colocará enfileirados
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            //Instanciando um leitor que lerá os bytes recebidos
            byte[] buffer = new byte[1024];
            //Criando a variável responsável pelo tamanho dos bytes
            int len;
            //Criando loop que lerá os bytes que estão no objeto 'in', este objeto é o que recebe os dados da webservice
            while ((len = in.read(buffer)) > 0){
                //escrevendo os dados no objeto 'os' criado no início deste método
                bos.write(buffer, 0 , len);
            }
            //Instanciando e convertendo(convertendo para tipo legível por uma string) o objeto bos que recebeu os dados que foram organizados pelo loop
            byte[] bytes = bos.toByteArray();
            //Retornando os bytes que serão atribuídos a uma string
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            try{
                //Fechando objetos de leitura de bytes.
                bos.close();
                in.close();
            }catch (IOException e){

            }
        }

    }
}
