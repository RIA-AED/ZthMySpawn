# ZthMySpawn
Zth 服务器的多主城实现，依赖 HuskHomes

## 原理

本插件实现了一套指令，可以通过编辑 `config.yml` 的 `real-list.nodes` 来配置一系列 HuskHomes 的 Warp 名。

`spawn-list` 的键名是展示在指令补全时和玩家使用 `/spawn [主城名]` 时使用的主城名。可以为中文。

## 配置文件示例

```yaml
spawn-list:
  spawn: spawn
  tsg: tsg
  zdmt: zdmt
  hfw: hfw
  naraku: naraku
  xzhj: xzhj
  初生水殿: spawn
  图书馆: tsg
  主岛码头: zdmt
  海风湾: hfw
  奈落洲主城: naraku
  新洲海角: xzhj
real-list:
  default: spawn
  nodes:
    - spawn
    - tsg
    - zdmt
    - hfw
    - naraku
    - xzhj

```
