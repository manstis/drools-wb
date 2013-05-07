    public void testGetPackageBinary () throws Exception {
        URL url = new URL("http://127.0.0.1:8080/guvnor-5.4.0-SNAPSHOT-jboss-as-7.0/rest/packages/mortgages/binary");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization",
                "Basic " + new Base64().encodeToString(( "admin:admin".getBytes() )));
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", MediaType.APPLICATION_OCTET_STREAM);
        connection.connect();

        System.out.println("ResponseCode: " + connection.getResponseCode());
        System.out.println("MediaType: " + connection.getContentType());
        InputStream is = connection.getInputStream();
    }