package net.xiaoxiangshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

/**
 * Utils - 安全
 * 
 */
public final class SecurityUtils {

	/**
	 * AES密钥签名算法
	 */
	public static final String AES_KEY_ALGORITHM = "AES";

	/**
	 * AES加密/解密签名算法
	 */
	public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";

	/**
	 * RSA密钥签名算法
	 */
	public static final String RSA_KEY_ALGORITHM = "RSA";

	/**
	 * RSA加密/解密签名算法
	 */
	public static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

	/**
	 * 安全服务提供者
	 */
	public static final Provider PROVIDER = new BouncyCastleProvider();

	/**
	 * 不可实例化
	 */
	private SecurityUtils() {
	}

	/**
	 * 生成密钥对
	 * 
	 * @param keySize
	 *            密钥大小
	 * @param algorithm
	 *            签名算法
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair(int keySize, String algorithm) {
		Assert.state(keySize > 0, "[Assertion failed] - keySize must be greater than 0");
		Assert.notNull(algorithm, "[Assertion failed] - algorithm is required; it must not be null");

		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, PROVIDER);
			keyPairGenerator.initialize(keySize);
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成私钥
	 * 
	 * @param encodedKey
	 *            密钥编码
	 * @param algorithm
	 *            签名算法
	 * @return 私钥
	 */
	public static PrivateKey generatePrivateKey(byte[] encodedKey, String algorithm) {
		Assert.notNull(encodedKey, "[Assertion failed] - encodedKey is required; it must not be null");
		Assert.notNull(algorithm, "[Assertion failed] - algorithm is required; it must not be null");

		try {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, PROVIDER);
			return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成私钥
	 * 
	 * @param keyString
	 *            密钥字符串(BASE64编码)
	 * @param algorithm
	 *            签名算法
	 * @return 私钥
	 */
	public static PrivateKey generatePrivateKey(String keyString, String algorithm) {
		Assert.hasText(keyString, "[Assertion failed] - keyString must have text; it must not be null, empty, or blank");
		Assert.notNull(algorithm, "[Assertion failed] - algorithm is required; it must not be null");

		return generatePrivateKey(Base64.decodeBase64(keyString), algorithm);
	}

	/**
	 * 生成公钥
	 * 
	 * @param encodedKey
	 *            密钥编码
	 * @param algorithm
	 *            签名算法
	 * @return 公钥
	 */
	public static PublicKey generatePublicKey(byte[] encodedKey, String algorithm) {
		Assert.notNull(encodedKey, "[Assertion failed] - encodedKey is required; it must not be null");
		Assert.notNull(algorithm, "[Assertion failed] - algorithm is required; it must not be null");

		try {
			KeyFactory keyFactory = KeyFactory.getInstance(algorithm, PROVIDER);
			return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeySpecException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成公钥
	 * 
	 * @param keyString
	 *            密钥字符串(BASE64编码)
	 * @param algorithm
	 *            签名算法
	 * @return 公钥
	 */
	public static PublicKey generatePublicKey(String keyString, String algorithm) {
		Assert.hasText(keyString, "[Assertion failed] - keyString must have text; it must not be null, empty, or blank");
		Assert.hasText(algorithm, "[Assertion failed] - algorithm must have text; it must not be null, empty, or blank");

		return generatePublicKey(Base64.decodeBase64(keyString), algorithm);
	}

	/**
	 * 获取密钥字符串
	 * 
	 * @param key
	 *            密钥
	 * @return 密钥字符串(BASE64编码)
	 */
	public static String getKeyString(Key key) {
		Assert.notNull(key, "[Assertion failed] - key is required; it must not be null");

		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * 获取加密密钥和证书的存储容器
	 * 
	 * @param type
	 *            类型
	 * @param inputStream
	 *            输入流
	 * @param password
	 *            密码
	 * @return 加密密钥和证书的存储容器
	 */
	public static KeyStore getKeyStore(String type, InputStream inputStream, String password) {
		Assert.hasText(type, "[Assertion failed] - type must have text; it must not be null, empty, or blank");
		Assert.notNull(inputStream, "[Assertion failed] - inputStream is required; it must not be null");

		try {
			KeyStore keyStore = KeyStore.getInstance(type, PROVIDER);
			keyStore.load(inputStream, password != null ? password.toCharArray() : null);
			return keyStore;
		} catch (KeyStoreException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (CertificateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取密钥
	 * 
	 * @param type
	 *            类型
	 * @param inputStream
	 *            输入流
	 * @param password
	 *            密码
	 * @return 密钥
	 */
	public static Key getKey(String type, InputStream inputStream, String password) {
		Assert.hasText(type, "[Assertion failed] - type must have text; it must not be null, empty, or blank");
		Assert.notNull(inputStream, "[Assertion failed] - inputStream is required; it must not be null");

		try {
			KeyStore keyStore = KeyStore.getInstance(type, PROVIDER);
			keyStore.load(inputStream, password != null ? password.toCharArray() : null);
			String alias = keyStore.aliases().hasMoreElements() ? keyStore.aliases().nextElement() : null;
			return keyStore.getKey(alias, password != null ? password.toCharArray() : null);
		} catch (KeyStoreException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (CertificateException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (UnrecoverableKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 获取证书
	 * 
	 * @param type
	 *            类型
	 * @param inputStream
	 *            输入流
	 * @return 证书
	 */
	public static Certificate getCertificate(String type, InputStream inputStream) {
		Assert.hasText(type, "[Assertion failed] - type must have text; it must not be null, empty, or blank");
		Assert.notNull(inputStream, "[Assertion failed] - inputStream is required; it must not be null");

		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance(type, PROVIDER);
			return certificateFactory.generateCertificate(inputStream);
		} catch (CertificateException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 生成签名
	 * 
	 * @param algorithm
	 *            签名算法
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 签名
	 */
	public static byte[] sign(String algorithm, PrivateKey privateKey, byte[] data) {
		Assert.hasText(algorithm, "[Assertion failed] - algorithm must have text; it must not be null, empty, or blank");
		Assert.notNull(privateKey, "[Assertion failed] - privateKey is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		try {
			Signature signature = Signature.getInstance(algorithm, PROVIDER);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SignatureException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param algorithm
	 *            签名算法
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            签名
	 * @param data
	 *            数据
	 * @return 是否验证通过
	 */
	public static boolean verify(String algorithm, PublicKey publicKey, byte[] sign, byte[] data) {
		Assert.hasText(algorithm, "[Assertion failed] - algorithm must have text; it must not be null, empty, or blank");
		Assert.notNull(publicKey, "[Assertion failed] - publicKey is required; it must not be null");
		Assert.notNull(sign, "[Assertion failed] - sign is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		try {
			Signature signature = Signature.getInstance(algorithm, PROVIDER);
			signature.initVerify(publicKey);
			signature.update(data);
			return signature.verify(sign);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SignatureException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 验证签名
	 * 
	 * @param algorithm
	 *            签名算法
	 * @param certificate
	 *            证书
	 * @param sign
	 *            签名
	 * @param data
	 *            数据
	 * @return 是否验证通过
	 */
	public static boolean verify(String algorithm, Certificate certificate, byte[] sign, byte[] data) {
		Assert.hasText(algorithm, "[Assertion failed] - algorithm must have text; it must not be null, empty, or blank");
		Assert.notNull(certificate, "[Assertion failed] - certificate is required; it must not be null");
		Assert.notNull(sign, "[Assertion failed] - sign is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		try {
			Signature signature = Signature.getInstance(algorithm, PROVIDER);
			signature.initVerify(certificate);
			signature.update(data);
			return signature.verify(sign);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SignatureException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @param algorithm
	 *            签名算法
	 * @return 密文
	 */
	public static byte[] encrypt(Key key, byte[] data, String algorithm) {
		Assert.notNull(key, "[Assertion failed] - key is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		return encrypt(key, data, algorithm, null);
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @param algorithm
	 *            签名算法
	 * @param algorithmParameterSpec
	 *            加密参数
	 * @return 密文
	 */
	public static byte[] encrypt(Key key, byte[] data, String algorithm, AlgorithmParameterSpec algorithmParameterSpec) {
		Assert.notNull(key, "[Assertion failed] - key is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		try {
			Cipher cipher = Cipher.getInstance(algorithm, PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, key, algorithmParameterSpec);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @param algorithm
	 *            签名算法
	 * @return 明文
	 */
	public static byte[] decrypt(Key key, byte[] data, String algorithm) {
		Assert.notNull(key, "[Assertion failed] - key is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		return decrypt(key, data, algorithm, null);
	}

	/**
	 * 解密
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @param algorithm
	 *            签名算法
	 * @param algorithmParameterSpec
	 *            加密参数
	 * @return 明文
	 */
	public static byte[] decrypt(Key key, byte[] data, String algorithm, AlgorithmParameterSpec algorithmParameterSpec) {
		Assert.notNull(key, "[Assertion failed] - key is required; it must not be null");
		Assert.notNull(data, "[Assertion failed] - data is required; it must not be null");

		try {
			Cipher cipher = Cipher.getInstance(algorithm, PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, key, algorithmParameterSpec);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}