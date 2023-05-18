## Workflow
- Update version in `build.gradle`.
- Update version in `SkyHead.java`.
- Add version and changelog to `update.json`.
- When adding commands add the name to `tabComplete` and `options` and add its actions in `processCommand`.
- When adding new properties to config file add variables to main class and retrieve them in `preInit` event.
- When adding new modes you have to create a command for them, assign a new number to it, add a cache for it, and add it to the API.
- Delete unused code and add comments to new code.
## Releasing
- Bugtesting.
- Draft a new release with the jar file attached.
- Add tag with correct version.
- Update `README.md`.
- Update latest and recommended version in `update.json`.
- Release new version.