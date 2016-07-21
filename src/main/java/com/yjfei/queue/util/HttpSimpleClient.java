public class HttpSimpleClient {
	
	
	private static Proxy proxy;
	
	public static void initProxy(String host,int port){
		 proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 8080));
	}

	public static HttpResult httpGet(String url, List<String> headers,
			List<String> paramValues, String encoding, long readTimeout)
			throws IOException {

		String encodedContent = encodingParams(paramValues, encoding);

		url += (null == encodedContent) ? "" : ("?" + encodedContent);

		HttpURLConnection conn = null;



		try {
			if(proxy == null){
				conn = (HttpURLConnection) new URL(url).openConnection();
			}else{
				conn = (HttpURLConnection) new URL(url).openConnection(proxy);
			}
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(100);
			conn.setReadTimeout((int) readTimeout);
			setHeaders(conn, headers, encoding);

			conn.connect();
			int respCode = conn.getResponseCode();

			String resp = null;

			if (HttpURLConnection.HTTP_OK == respCode) {
				resp = IOUtils.toString(conn.getInputStream(), encoding);
			} else {
				resp = IOUtils.toString(conn.getErrorStream(), encoding);
			}

			return new HttpResult(respCode, resp);

		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}


	}
	
	
	public static HttpResult httpPost(String url ,List<String> headers,
			List<String> paramValues, String encoding, long readTimeout)
			throws IOException {
		
		String encodedContent = encodingParams(paramValues, encoding);

		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(100);
			conn.setReadTimeout((int) readTimeout);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			setHeaders(conn, headers, encoding);
			
			
			conn.getOutputStream().write(encodedContent.getBytes());
			
			int respCode = conn.getResponseCode();
			
			String resp = null;

			if (HttpURLConnection.HTTP_OK == respCode) {
				resp = IOUtils.toString(conn.getInputStream(), encoding);
			} else {
				resp = IOUtils.toString(conn.getErrorStream(), encoding);
			}

			return new HttpResult(respCode, resp);
			
		}finally{
			if (conn != null) {
				conn.disconnect();
			}
		}
		
	}

	private static void setHeaders(HttpURLConnection conn,
			List<String> headers, String encoding) {
		if (null != headers) {

			for (Iterator<String> it = headers.iterator(); it.hasNext();) {
				conn.addRequestProperty(it.next(), it.next());
			}
		}
		conn.addRequestProperty("Content-Type",
				"application/x-www-form-urlencode;charset=" + encoding);
		conn.addRequestProperty("Client-Version", "3.5.0");

	}

	private static String encodingParams(List<String> paramValues,
			String encoding) throws UnsupportedEncodingException {

		if (null == paramValues) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> it = paramValues.iterator(); it.hasNext();) {
			sb.append(it.next()).append("=");
			sb.append(URLEncoder.encode(it.next(), encoding));
			if (it.hasNext()) {
				sb.append("&");
			}
		}

		return sb.toString();
	}

	public static class HttpResult {
		public final int code;
		public final String content;

		public HttpResult(int code, String content) {
			this.code = code;
			this.content = content;
		}
	}
	
	
	
	public static void main(String args[]){
		
		String url = "http://api.wallstreetcn.com/v2/livenews";
		
		try {
			
			HttpSimpleClient.initProxy("10.59.103.238", 8080);
			HttpResult result = HttpSimpleClient.httpGet(url, null, null, "utf-8", 50000L);
			
			
			System.out.println(result.content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
