# Mine Show Me (Fabric)

Language: [English](#english-en) | [Português (BR)](#português-pt-br)

---

## English (EN)

HUD utility mod to display quick in‑game information (coordinates, biome, time, performance and more) in a lightweight and configurable way.

### Features
Implemented:
- Coordinates (XYZ + facing)
- Light level (block & sky)
- Current biome
- World day counter
- FPS
- (Base HUD structure)

Planned / In progress:
- In‑game time (hh:mm)
- Ping (servers)
- Memory usage (allocated / used / %)
- CPU usage (client estimation)
- GPU info (name / basic)
- Layout & themes (light / dark / compact)
- Minimal mode

### Requirements
- Minecraft: 1.xx.x (define)
- Fabric Loader: 0.xx.x+
- Fabric API: 0.xx.x
- Java: 17+

### Installation (User)
1. Download the mod .jar (Release or CI build).
2. Drop into .minecraft/mods
3. Ensure Fabric Loader + Fabric API installed.
4. Launch game.

### Build (Dev)
Prerequisites: Java 17, Gradle (wrapper included).

```bash
./gradlew build
# artifact:
build/libs/<mod-name>-<version>.jar
```

Dev runtime:
```bash
./gradlew runClient
```

### Configuration
(Planned) File: config/mine-show-me.json
Example (future):
```json
{
  "show_coordinates": true,
  "show_light": true,
  "show_biome": true,
  "show_day": true,
  "show_clock": true,
  "show_ping": false,
  "theme": "dark",
  "position": "TOP_LEFT"
}
```

### Performance
Optimized drawing: avoids recomputing static text every tick.  
Future ideas:
- String cache
- Staggered updates (e.g. ping every 1s)

### Short Roadmap
- [ ] Add clock
- [ ] Add ping
- [ ] In‑game config GUI
- [ ] Theme system
- [ ] Modular widgets
- [ ] i18n (pt-BR / en-US)

### Suggested Structure
- package hud.widgets (each component)
- WidgetRegistry to register/order
- Central render loop only calling visible ones

### Contributing
1. Fork
2. Branch: feature/name
3. Small descriptive commits
4. PR with summary + screenshots

Commit convention (suggested):  
feat:, fix:, perf:, docs:, refactor:, build:, chore:, test:

### Testing
- Overlay with other HUD mods
- Debug screen F3 coexistence
- Remote server (ping)

### License
Define (MIT / LGPL / ARR).  
(Add LICENSE file later)

### FAQ
Q: Forge support?  
A: No, Fabric only.

Q: FPS impact?  
A: Very low.

Q: Modpack usage?  
A: Yes (respect license).

### Credits
Author: (Add)  
Initial ideas: ideias.txt

### Contact
(Add GitHub / Discord)

---

## Português (PT-BR)

HUD utilitário para exibir informações rápidas do jogo (coordenadas, bioma, horário, performance e mais) de forma leve e configurável.

## Funcionalidades
Implementadas:
- Coordenadas (XYZ + direção)
- Brilho/Light level (bloco e céu)
- Bioma atual
- Dia do mundo (contador)
- FPS
- (Estrutura base do HUD)

Planejadas / Em desenvolvimento:
- Horário do jogo (horas/minutos)
- Ping (em servidores)
- Uso de memória (alocada / usada / %)
- Uso de CPU (estimado - lado cliente)
- GPU (nome / info básica)
- Alternar layout e temas (claro/escuro/compacto)
- Modo minimalista

## Requisitos
- Minecraft: 1.xx.x (definir)
- Fabric Loader: 0.xx.x ou superior
- Fabric API: 0.xx.x
- Java: 17+

## Instalação (Usuário Final)
1. Baixe o .jar deste mod (Release ou Builds).
2. Coloque em: .minecraft/mods
3. Certifique-se de ter Fabric Loader + Fabric API.
4. Inicie o jogo.

## Compilação (Dev)
Pré-requisitos: Java 17, Gradle (ou wrapper incluso).

```bash
# primeira vez
./gradlew build
# artefato resultante
build/libs/<nome-do-mod>-<versão>.jar
```

Para desenvolvimento contínuo:
```bash
./gradlew runClient
```

## Configuração
(Planejado) Arquivo: config/mine-show-me.json  
Exemplo (futuro):
```json
{
  "show_coordinates": true,
  "show_light": true,
  "show_biome": true,
  "show_day": true,
  "show_clock": true,
  "show_ping": false,
  "theme": "dark",
  "position": "TOP_LEFT"
}
```

## Desempenho
Desenho otimizado: evita recalcular textos a cada tick quando não mudam.  
Sugestões futuras:
- Cache de strings
- Atualização escalonada (ex: ping a cada 1s)

## Roadmap Curto
- [ ] Adicionar relógio
- [ ] Adicionar ping
- [ ] Config GUI dentro do jogo
- [ ] Sistema de temas
- [ ] Modularização de widgets
- [ ] Internacionalização (pt-BR / en-US)

## Estrutura Sugerida (Código)
- package hud.widgets (cada componente)
- WidgetRegistry para registrar e ordenar
- Render loop central chamando somente visíveis

## Contribuição
1. Fork
2. Branch feature/nome
3. Commit pequeno e descritivo
4. Pull Request com resumo + screenshots

Padrão de commit (sugestão):
feat:, fix:, perf:, docs:, refactor:, build:, chore:, test:

## Testes
- Verificar sobreposição com outros mods de HUD
- Checar modo debug + F3
- Testar em servidor remoto (ping)

## Licença
Definir (ex: MIT / LGPL / ARR).  
(Adicionar arquivo LICENSE posteriormente)

## FAQ
P: Funciona em Forge?  
R: Não. Apenas Fabric.

P: Consome muito FPS?  
R: Leve. Textos mínimos e sem loops pesados.

P: Pode usar em modpack?  
R: Sim (respeite a licença escolhida).

## Créditos
Autor: (Adicionar)  
Ideias iniciais: arquivo ideias.txt

## Contato
(Adicionar link GitHub / Discord)

---
Atualize este README conforme as features forem concluídas.
