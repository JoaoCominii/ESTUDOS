private static JSONArray getAllArtes() throws Exception {
        String url = "jdbc:postgresql://pucmg5.postgres.database.azure.com:5432/postgres";
        String user = "adm"; // ou apenas "artur70152@ti270.postgres.database.azure.com" 
        	 String password = "@Pucminas70152";

        Class.forName("org.postgresql.Driver");

        JSONArray artesList = new JSONArray();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
          //  String sql = "SELECT a.id as arte_id, a.arte, u.email, c.usuario as coment_usuario, c.texto as coment_texto " +
            //             "FROM arte a " +
            //             "JOIN usuario u ON a.usuario_id = u.id " +
           //              "LEFT JOIN comentario_arte c ON c.arte_id = a.id " +
            //             "ORDER BY a.id";

            
        	String sql = 
        		    "SELECT a.id as arte_id, a.arte, a.name, a.curtidas, a.descricao, u.email, " +
        		    "c.usuario as coment_usuario, c.texto as coment_texto " +
        		    "FROM arte a " +
        		    "JOIN usuario u ON a.usuario_id = u.id " +
        		    "LEFT JOIN comentario_arte c ON c.arte_id = a.id " +
        		    "ORDER BY a.id";



            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    int lastArteId = -1;
                    JSONObject currentArte = null;
                    JSONArray comentarios = null;

                    while (rs.next()) {
                        int arteId = rs.getInt("arte_id");

                        // Nova arte
                        if (arteId != lastArteId) {
                            if (currentArte != null) {
                                currentArte.put("comentarios", comentarios);
                                artesList.put(currentArte);
                            }

                            currentArte = new JSONObject();
                            currentArte.put("id", arteId);
                            String caminhoImagem = rs.getString("arte");  // Ex: "image123.png"
                            
                            String urlBlob = "https://arturstorage123.blob.core.windows.net/upload/" + caminhoImagem;
                            System.out.println(urlBlob);
                            currentArte.put("arte", urlBlob);

                            currentArte.put("name", rs.getString("name"));
                            currentArte.put("usuario", rs.getString("email"));
                            currentArte.put("curtidas", rs.getInt("curtidas"));
                            currentArte.put("descricao", rs.getString("descricao")); // ✅ ADD ISSO

                            comentarios = new JSONArray();

                            lastArteId = arteId;
                        }

                        // Comentário (se houver)
                        String comentUsuario = rs.getString("coment_usuario");
                        String comentTexto = rs.getString("coment_texto");

                        if (comentUsuario != null && comentTexto != null) {
                            JSONObject comentario = new JSONObject();
                            comentario.put("usuario", comentUsuario);
                            comentario.put("texto", comentTexto);
                            comentarios.put(comentario);
                        }
                    }

                    // Última arte
                    if (currentArte != null) {
                        currentArte.put("comentarios", comentarios);
                        artesList.put(currentArte);
                    }
                }
            }
        }
        return artesList;
    }
