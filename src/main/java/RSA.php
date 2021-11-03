<php
class RSAEncrypt {
    public static function publicEncrypt($input, $publicKey) {
        $key = openssl_pkey_get_public(RSAEncrypt::formatKey($publicKey, 'public'));
        openssl_public_encrypt($input, $output, $key);
        return base64_encode($output);
    }

    public static function publicDecrypt($input, $publicKey) {
        $key = openssl_pkey_get_public(RSAEncrypt::formatKey($publicKey, 'public'));
        openssl_public_decrypt(base64_decode($input), $output, $key);
        return $output;
    }

    public static function privateEncrypt($input, $privateKey) {
        $key = openssl_pkey_get_private(RSAEncrypt::formatKey($privateKey, 'private'));
        openssl_private_encrypt($input, $output, $key);
        return $output;
    }

    public static function privateDecrypt($input, $privateKey) {
        $key = openssl_pkey_get_private(RSAEncrypt::formatKey($privateKey, 'private'));
        openssl_private_decrypt(base64_decode($input), $output, $key);
        return base64_encode($output);
    }

    public static function formatKey($key, $type) {
        $preKey = (wordwrap($key, 64, "\n", true)) . "\n";
        if ($type == 'public') {
            $pem_key = "-----BEGIN PUBLIC KEY-----\n" . $preKey . "-----END PUBLIC KEY-----\n";
        } else if ($type == 'private') {
            $pem_key = "-----BEGIN RSA PRIVATE KEY-----\n" . $preKey . "-----END RSA PRIVATE KEY-----\n";
        } else {
            exit();
        }
        return $pem_key;
    }
}
?>