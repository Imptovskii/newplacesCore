# /new places/ Core

![изображение](https://psv4.userapi.com/c520036/u306539345/docs/d11/386ec9c1a477/Novy_proekt.png?extra=vEiyqd_ha0ukQo4_3cWiBl3GirO0shdYET3A_P4pyeMIAJOfToGfZEbQbJF7_ArwL_oN1whDUD39YecBCPqTJH343qYEhpKOO9YyaXGze385Go3-RwyMehZCjO2KYUnKtoVrvb015watkpGlnsdSWvI)

Это самопал из разных фиксов из плагинов адаптированных под мои цели для моего сервера /new places/

Работает на 1.17.1-1.18
Для работы требуется ProtocolLib 4.7.0-4.8.0

Что он исправляет:
1. AntiBookExploit - запрещает писать Unicode в книги, только кириллица и латиница в соотвествии с кодировкой ISO-8859-5. Для противодействия создания BookBan или ChunkDupe.
2. AntiBurrow - противодействует читу Burrow который не может ловить NoCheatPlus
3. AntiBowBomb - противодействует эксплоиту One Tap (Bow Bomb) который позволяет убивать/"попнуть" тотем с одного высрела, этот эксплоит не может ловить эффективно NoCheatPlus
4. AntiPacketExploit - противодействует читам которые базируются на пакетном принципе работы, в частности на полет.
5. AntiRedstone - противодействует лаг машинам, отключая все редстоун механизмы на сервере при низком TPS сервера.
6. AntiNetherRoof - противодействует попыткам передвигаться по крыше измерения Nether, просто не позволяя игрокам передвигаться.
7. PacketElytraFly - Для противодействия пакетным ElytraFly из Future, Konas, KamiBlue, RusherHack

Есть 2 версии.
Anarchy - для нашего анархичного сервера, и CPVP - для нашего CPVP сервера.

Проект легко собирается из IntelliJ IDEA в Maven
