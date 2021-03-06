package DAO;



import Dominio.Usuario;

import java.io.FileOutputStream;

import java.io.IOException;

import java.text.ParseException;

import java.util.ArrayList;

import java.util.Iterator;

import java.util.List;

import org.jdom.Document;

import org.jdom.Element;

import org.jdom.JDOMException;

import org.jdom.input.SAXBuilder;

import org.jdom.output.XMLOutputter;



public class DAOUsuario {

    private Element root;

    private String fileLocation = "src//XML//Usuario.xml";

    

    public DAOUsuario() {

        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = null;
            doc = builder.build(fileLocation);
            root = doc.getRootElement();
        } catch (JDOMException ex) {
            System.out.println("No se pudo iniciar la operacion por: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("No se pudo iniciar la operacion por: " + ex.getMessage());
        }
    }

    

    private Element UsuariotoXmlElement(Usuario nUsuario ) {

        Element Usuariotrans = new Element("Usuario");
        Element hashIp = new Element("hashIp");
        hashIp.setText(nUsuario.getHashIp());
        Element ip = new Element("ip");
        ip.setText((nUsuario.getIp()));
        Usuariotrans.addContent(hashIp);
        Usuariotrans.addContent(ip);
        return Usuariotrans;

    }

    

    private Usuario UsuarioToObject(Element element) throws ParseException {
        Usuario nUsuario = new Usuario (element.getChildText("ip"));
        return nUsuario;

    }

    

    public boolean agregarUsuario(Usuario nUsuario) {
        boolean resultado = false;
        root.addContent(UsuariotoXmlElement((Usuario) nUsuario));
        resultado = updateDocument();
        return resultado;

    }

    

    private boolean updateDocument() {

        try {
            XMLOutputter out = new XMLOutputter(org.jdom.output.Format.getPrettyFormat());
            FileOutputStream file = new FileOutputStream(fileLocation);
            out.output(root, file);
            file.flush();
            file.close();
            return true;
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return false;
        }

    }

    

    public static Element buscar(List raiz, String dato) {
        Iterator i = raiz.iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            if (dato.equals(e.getChild("hashIp").getText())) {
                return e;
            }
        }
        return null;

    }

 

    

    public boolean actualizarUsuario(Usuario nUsuario,String hashIp) {
        boolean resultado = false;
        Element aux = new Element("Usuario");
        List Usuario = this.root.getChildren("Usuario");
        while (aux != null) {
            aux = DAOUsuario.buscar(Usuario,hashIp);
            if (aux != null) {
                Usuario.remove(aux);
                resultado = updateDocument();
            }
        }
        agregarUsuario(nUsuario);
        return resultado;
    } 

    

    public Usuario buscarUsuario(String usuario) {

        Element aux = new Element("Usuario");

        List Usuario= this.root.getChildren("Usuario");

        while (aux != null) {

            aux = DAOUsuario.buscar(Usuario,usuario);

            if (aux != null) {

                try {

                    return UsuarioToObject(aux);

                } catch (ParseException ex) {

                    System.out.println(ex.getMessage());

                }

            }

        }

        return null;

    }

   

    

    

    public ArrayList<Usuario> todosLosUsuarios() {

        ArrayList<Usuario> resultado = new ArrayList<Usuario>();

        for (Object it : root.getChildren()) {

            Element xmlElem = (Element) it;

            try {

                resultado.add(UsuarioToObject(xmlElem));

            } catch (ParseException ex) {

                System.out.println(ex.getMessage());

            }

        }

               

        return resultado;

    } 

    

    

    public void ordenarLista(int arreglos[])

    {

        ArrayList<Usuario> lista = new ArrayList<Usuario>();

        lista = todosLosUsuarios();

        Usuario arreglo[] = new Usuario[lista.size()];

        arreglo = lista.toArray(arreglo);

        eliminarUsuarios();

        for(int i = 0; i < arreglo.length - 1; i++){

            for(int j = 0; j < arreglo.length - 1; j++){

                if (Integer.parseInt(arreglo[j].getHashIp()) < Integer.parseInt(arreglo[j + 1].getHashIp()))

                {

                    Usuario tmp = arreglo[j+1];

                    arreglo[j+1] = arreglo[j];

                    arreglo[j] = tmp;

                }

            }

        }

        for(int i = 0;i < arreglo.length; i++){

            //System.out.print(arreglo[i]+"\n");

            agregarUsuario(arreglo[i]);

        }

        

        

    }

    

    public boolean borrarUsuario(String hashIp) {

        boolean resultado = false;

        Element aux = new Element("Usuario");

        List Usuario = this.root.getChildren("Usuario");

        while (aux != null) {

            aux = DAOUsuario.buscar(Usuario,hashIp);

            if (aux != null) {

                Usuario.remove(aux);

                resultado = updateDocument();

            }

        }

        return resultado;

    }

    

    public void eliminarUsuarios(){

        ArrayList<Usuario> lista = new ArrayList<Usuario>();

        lista = todosLosUsuarios();

        for (Usuario s:lista){

            borrarUsuario(s.getHashIp());

        }

    }

}

