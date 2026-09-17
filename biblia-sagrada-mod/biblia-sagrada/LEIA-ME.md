# Mod: Bíblia Sagrada 📖✝️

Adiciona ao Minecraft (Java, Forge 1.20.1) itens de tema sagrado que combatem monstros.

## Item 1 — Bíblia Sagrada

- **Segurando o item** (mão principal ou secundária): cria uma aura sagrada de ~5 blocos
  que empurra suavemente qualquer monstro hostil (zumbi, esqueleto, aranha, creeper, etc.)
  a cada instante, mantendo-os afastados.
- **Clique direito**: libera uma "rajada de fé" — empurra com força e cega por 3 segundos
  todos os monstros num raio de 8 blocos. Tem 15 segundos de recarga (cooldown).
- Craft: 8 Lingotes de Ouro ao redor de 1 Livro, no formato de moldura (veja a receita).
- O item brilha como um item encantado (efeito visual de "foil").

## Item 2 — Espada de Miguel ⚔️

- **Ataque corpo a corpo**: todo monstro atingido é "agarrado por correntes" — por 3
  segundos é arrastado para baixo com partículas escuras (tinta de lula + fumaça) e
  fica enfraquecido, e ao fim desse tempo é **teleportado para o Nether** (o Inferno)
  e pega fogo. Se o monstro já estiver no Nether, ele só é arrastado/queimado no lugar.
- **Clique direito**: **invoca a entidade Jesus** ao seu lado — ele luta automaticamente
  contra qualquer monstro próximo, tem bastante vida e dano, é imune a fogo, e some
  sozinho (num brilho de partículas) depois de 45 segundos. Recarga de 60 segundos.
- Craft: 2 Diamantes + 1 Graveto, em formato de espada (veja a receita).
- Base estatística de uma espada de netherite, com dano extra.
- Vem com uma textura *placeholder* simples para a espada e para a "pele" do Jesus
  (robe branca com detalhes dourados) — troque pelas suas próprias texturas quando
  quiser, nos caminhos indicados mais abaixo.

### Sobre o teleporte pro Nether

O código usa `Entity#teleportTo(ServerLevel, ...)`, um método que o próprio Forge
adiciona para lidar com teleporte seguro entre dimensões. Isso é o padrão usado em
tutoriais de mods Forge 1.20.x, mas se sua build específica do Forge reclamar dessa
assinatura ao compilar, me avise que ajusto para `changeDimension(...)` (API vanilla).

## Como conseguir o arquivo .jar (um arquivo só, igual você pediu)

Importante ser honesto: eu, aqui no chat, **não consigo compilar o mod pra você** —
gerar o `.jar` final exige baixar o Minecraft e o Forge originais dos servidores da
Mojang/Forge, e o ambiente onde eu rodo código não tem acesso a esses servidores
(só a uma lista pequena de sites liberados). Testei agora mesmo e a conexão foi
bloqueada. Então o que eu te dou aqui é sempre o **código-fonte** — e alguém (você,
seu PC, ou um serviço na nuvem) precisa compilar esse código pra virar o `.jar` único
que você quer, do mesmo jeito que o `Better_Cave_Dweller-1_20_1.jar` que você me
mandou também precisou ser compilado por quem o fez.

Preparei a forma mais fácil de fazer isso **sem instalar nada no seu PC**, usando o
GitHub (de graça):

1. Crie uma conta em https://github.com (se não tiver).
2. Clique em "New repository" (Novo repositório) — pode deixar como **Private**.
3. Dentro do repositório vazio, clique em "uploading an existing file" (ou na aba
   "Add file" > "Upload files").
4. Extraia o `.zip` que eu te mandei no seu PC, e arraste **todo o conteúdo da pasta
   `biblia-sagrada`** (incluindo a pasta `.github`, que geralmente fica escondida —
   ative "mostrar arquivos ocultos" no seu gerenciador de arquivos) para dentro da
   área de upload do GitHub, e confirme o commit.
5. Vá na aba **"Actions"** do repositório. O GitHub já vai começar a compilar
   automaticamente (ele baixa o Minecraft e o Forge nos próprios servidores dele,
   que têm acesso liberado). Se não começar sozinho, clique em "Run workflow".
6. Espere terminar (uns 3 a 6 minutos, aparece uma bolinha verde ✅ quando acaba).
7. Clique na execução que terminou, role até a seção **"Artifacts"** no final da
   página, e baixe o arquivo `biblia-sagrada-mod-jar` — dentro dele está o `.jar`
   único, pronto pra jogar direto na pasta `mods` do seu Minecraft (junto com o
   Forge 1.20.1 e a versão certa do Minecraft instalados).

Se preferir compilar no seu próprio PC em vez de usar o GitHub (também funciona,
só precisa de internet liberada e mais paciência):

1. Baixe o **Forge MDK 1.20.1** (versão 47.2.0) em https://files.minecraftforge.net/
2. Extraia o MDK numa pasta e copie o conteúdo desta pasta (`biblia-sagrada/`)
   por cima, mesclando as pastas `src/` e os arquivos `build.gradle`,
   `gradle.properties`, `settings.gradle`.
   - Alternativamente, se você já tem seu próprio ambiente Forge configurado
     (workspace do Eclipse/IntelliJ com o Gradle já baixado), basta copiar a
     pasta `src/main/java/com/usuario/bibliasagrada` e
     `src/main/resources/...` para dentro do seu projeto existente, e ajustar
     o `mods.toml` se você já tiver um modId diferente.
3. Abra o projeto no IntelliJ IDEA (recomendado) ou Eclipse.
4. Rode a task do Gradle `genIntellijRuns` (ou `genEclipseRuns`) para gerar as
   configurações de execução.
5. Rode a configuração **"Minecraft Client"** para testar no jogo.
6. Para gerar o `.jar` final: `./gradlew build` — o arquivo sai em `build/libs/`.

## Arquivos incluídos

```
src/main/java/com/usuario/bibliasagrada/
├── BibliaSagradaMod.java          -> classe principal do mod
├── item/
│   ├── ModItems.java              -> registro dos itens
│   ├── BibliaSagradaItem.java     -> lógica da rajada de fé (clique direito)
│   └── EspadaDeMiguelItem.java    -> lógica de ataque + invocar Jesus
├── entity/
│   ├── ModEntities.java           -> registro da entidade Jesus
│   └── JesusEntity.java           -> comportamento da entidade Jesus
├── event/
│   ├── RepelEventHandler.java     -> aura passiva que empurra monstros (Bíblia)
│   └── InfernoPullHandler.java    -> puxa monstros pro Nether (Espada)
└── client/
    ├── ClientSetup.java           -> registra o renderer (só no lado cliente)
    └── JesusRenderer.java         -> visual da entidade Jesus

src/main/resources/
├── META-INF/mods.toml
├── assets/bibliasagrada/
│   ├── lang/ (pt_br.json, en_us.json)
│   ├── models/item/ (biblia_sagrada.json, espada_miguel.json)
│   └── textures/
│       ├── item/ (biblia_sagrada.png, espada_miguel.png)
│       └── entity/jesus.png       -> skin placeholder (64x64) da entidade Jesus
└── data/bibliasagrada/recipes/ (biblia_sagrada.json, espada_miguel.json)
```

## Personalize à vontade

- **Textura**: troquei por uma imagem placeholder simples (16x16, capa marrom
  com cruz dourada). Se quiser algo mais bonito, basta substituir o arquivo
  `biblia_sagrada.png` por uma textura desenhada por você.
- **Raio e força do repelo**: ajuste as constantes `RAIO_AURA` e `FORCA_AURA`
  em `RepelEventHandler.java`, e `RAIO_RAJADA` / `FORCA_EMPURRAO_RAJADA` em
  `BibliaSagradaItem.java`.
- **Receita**: edite `biblia_sagrada.json` em `data/.../recipes/` se quiser
  outros ingredientes.
- Dá pra fazer o efeito ser só contra mobs específicos (ex: só undead, tipo
  "água benta"), ou dar dano de verdade em vez de só empurrar — é só avisar
  que eu ajusto o código.
