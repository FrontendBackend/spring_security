package com.backend.security.utils;

import java.util.Calendar;
import org.apache.commons.codec.binary.Base64;

import com.backend.security.dtos.UserAuthDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CommonsUtil {

	/**
	 * Permite obtener un codigo unico a partir de la fecha actual
	 * @return
	 */
	public static String generadorNombreUnico() {
		Calendar cal = Calendar.getInstance();
		String nombre = String.valueOf(cal.get(Calendar.YEAR))
				+ String.valueOf(cal.get(Calendar.MONTH)+1) + String.valueOf(cal.get(Calendar.DAY_OF_MONTH))
						+ "-" +String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + String.valueOf(cal.get(Calendar.MINUTE))
						+ String.valueOf(cal.get(Calendar.SECOND)) + String.valueOf(cal.get(Calendar.MILLISECOND));
		return nombre;
	}

	/**
	 * Permite deserializar el token priviamente serializado.
	 * 
	 * @param idToken Token que viaje entre el frontend y backend.
	 * @return
	 * @throws Exception
	 */
	public static UserAuthDTO deserializarToken(String idToken) throws Exception {

		String jwtToken = idToken;
		UserAuthDTO userAuthDTO = new UserAuthDTO();
		String body = "";

		try {
			System.out.println("------------ Decode JWT ------------");
			String[] split_string = jwtToken.split("\\.");
			String base64EncodedHeader = split_string[0];
			String base64EncodedBody = split_string[1];

			System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
			Base64 base64Url = new Base64(true);
			String header = new String(base64Url.decode(base64EncodedHeader));
			System.out.println("JWT Header : " + header);

			System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
			body = new String(base64Url.decode(base64EncodedBody));
			System.out.println("JWT Body : " + body);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			userAuthDTO = mapper.readValue(body, UserAuthDTO.class);

		} catch (JsonParseException e) {
			log.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}

		return userAuthDTO;

	}
}
