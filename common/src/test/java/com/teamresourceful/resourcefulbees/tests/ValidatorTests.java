package com.teamresourceful.resourcefulbees.tests;


import com.teamresourceful.resourcefulconfig.web.config.validators.*;
import com.teamresourceful.resourcefulconfig.web.info.UserJwtPayload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public class ValidatorTests {

    @Test
    public void testAlwaysFalseValidator() {
        final UserJwtPayload payload = new UserJwtPayload(new UUID(0, 0), "Display Name", "password", 0);
        final AlwaysFalseValidator validator = AlwaysFalseValidator.INSTANCE;
        Assertions.assertFalse(validator.test(payload));
    }

    @Test
    public void testDerivedPasswordValidator() {
        final SecretKeySpec spec = new SecretKeySpec("password".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        final UserJwtPayload payload = new UserJwtPayload(new UUID(0, 0), "Display Name", "QEfpqAJijSFgCXDquHfGRWwcIb22/9m240qBocH3DbM=", 0);
        final DerivedPasswordValidator validator = new DerivedPasswordValidator(spec);
        Assertions.assertTrue(validator.test(payload));
    }

    @Test
    public void testSha1HashedPasswordValidator() {
        final UserJwtPayload payload = new UserJwtPayload(new UUID(0, 0), "Display Name", "password", 0);
        final HashedPasswordValidator validator = new HashedPasswordValidator(HashedPasswordValidator.HashType.SHA1, "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8");
        Assertions.assertTrue(validator.test(payload));
    }

    @Test
    public void testSha256HashedPasswordValidator() {
        final UserJwtPayload payload = new UserJwtPayload(new UUID(0, 0), "Display Name", "password", 0);
        final HashedPasswordValidator validator = new HashedPasswordValidator(HashedPasswordValidator.HashType.SHA256, "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        Assertions.assertTrue(validator.test(payload));
    }

    @Test
    public void testSha512HashedPasswordValidator() {
        final UserJwtPayload payload = new UserJwtPayload(new UUID(0, 0), "Display Name", "password", 0);
        final HashedPasswordValidator validator = new HashedPasswordValidator(HashedPasswordValidator.HashType.SHA512, "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86");
        Assertions.assertTrue(validator.test(payload));
    }

    @Test
    public void testMd5HashedPasswordValidator() {
        final UserJwtPayload payload = new UserJwtPayload(new UUID(0, 0), "Display Name", "password", 0);
        final HashedPasswordValidator validator = new HashedPasswordValidator(HashedPasswordValidator.HashType.MD5, "5f4dcc3b5aa765d61d8327deb882cf99");
        Assertions.assertTrue(validator.test(payload));
    }

    @Test
    public void testIfValidator() {
        final UserJwtPayload passPayload = new UserJwtPayload(new UUID(0, 0), "Display Name", "password", 0);
        final UserJwtPayload failPayload = new UserJwtPayload(new UUID(1, 0), "Display Name", "password", 0);
        final IfValidator validator = new IfValidator(Set.of(new UUID(0, 0)),
                new HashedPasswordValidator(HashedPasswordValidator.HashType.MD5, "5f4dcc3b5aa765d61d8327deb882cf99"), Optional.empty());
        Assertions.assertTrue(validator.test(passPayload));
        Assertions.assertFalse(validator.test(failPayload));
    }

    @Test
    public void testOrValidator() {
        final UserJwtPayload passPayload = new UserJwtPayload(new UUID(0, 0), "Display Name", "password", 0);
        final UserJwtPayload pass2Payload = new UserJwtPayload(new UUID(1, 0), "Display Name", "/8vDz8I/nwxEpyfsddqU32Pb/jYISn3ml5u8/1Clw5U=", 0);
        final UserJwtPayload failPayload = new UserJwtPayload(new UUID(1, 0), "Display Name", "badpassword", 0);
        final OrValidator validator = new OrValidator(List.of(
                new HashedPasswordValidator(HashedPasswordValidator.HashType.MD5, "5f4dcc3b5aa765d61d8327deb882cf99"),
                new DerivedPasswordValidator(new SecretKeySpec("password".getBytes(StandardCharsets.UTF_8), "HmacSHA256"))
        ));
        Assertions.assertTrue(validator.test(passPayload));
        Assertions.assertTrue(validator.test(pass2Payload));
        Assertions.assertFalse(validator.test(failPayload));
    }

    @Test
    public void testPasswordValidator() {
        final String password = "password";
        final UserJwtPayload payload = new UserJwtPayload(new UUID(0, 0), "Display Name", password, 0);
        final PasswordValidator validator = new PasswordValidator(password);
        Assertions.assertTrue(validator.test(payload));
    }
}
