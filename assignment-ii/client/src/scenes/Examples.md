# Simple Alert

```typescript
Swal.fire({
  title: filename + " (size: " + data.length + "/" + fileSize + ")",
  heightAuto: false,
  text: data,
});
```

# Ask and Validate

Like a bad tsx :(

```typescript
const result = await Swal.fire({
  title: "Create Satellite",
  heightAuto: false,
  html: `
    <label for="satellite-id" class="f6 b db mb2 js-name">Satellite Id <span class="normal black-60">(required, alphanumeric/spaces only)</span></label>
    <input
        id="satellite-id"
        required=""
        pattern="[a-zA-Z 0-9]+"
        class="input-reset ba b--black-20 pa2 mb2 db w-100 js-nameInput"
        type="text"
        />
    <label for="satellite-type" class="f6 b db mb2 mt3"
        >Satellite Type <span class="normal black-60">(required)</span></label
    >
    <select
        id="satellite-type"
        class="swal2-input"
        list="satellite-types"
        required=""
        >
        <datalist id="satellite-types">
        <option>StandardSatellite</option>
        <option>ShrinkingSatellite</option>
        <option>RelaySatellite</option>
        <option>ElephantSatellite</option>
        </datalist>
    </select>
    `,
  focusConfirm: false,
  showCancelButton: true,
  showLoaderOnConfirm: true,
  allowOutsideClick: () => !Swal.isLoading(),
  preConfirm: async () => {
    let inputValidator = async (values) => {
      if (!values || !values[0]) {
        Swal.showValidationMessage("You need to select a satellite type");
        return false;
      }
      if (
        !values ||
        !values[1] ||
        !String(values[1]).match("^[A-Za-z 0-9]+$")
      ) {
        Swal.showValidationMessage(
          "You need to select a valid satellite id that consists of just alphanumeric characters"
        );
        return false;
      }
      if (
        this.devicesData.map((x) => x.id).includes(values[1]) ||
        this.satellitesData.map((x) => x.id).includes(values[1])
      ) {
        Swal.showValidationMessage("DeviceID must be unique");
        return false;
      }
      return values;
    };
    let results = [
      (document.getElementById("satellite-type") as HTMLSelectElement).value,
      (document.getElementById("satellite-id") as HTMLInputElement).value,
    ];
    if (await inputValidator(results)) {
      return axios
        .put(
          `/api/satellite/?satelliteId=${results[1].replace(
            " ",
            "%20"
          )}&position=${mouseDeg}&height=${Math.trunc(mouseDist)}&type=${
            results[0]
          }`,
          {},
          {}
        )
        .then((resp) => {
          console.log("Succeeded");
          return true;
        })
        .catch((err) =>
          Swal.showValidationMessage(
            "CreateSatellite failed, check the java console for error information"
          )
        );
    }
  },
});
if (result.isConfirmed) {
  this.regenerateSatellites();
  this.regenerateDevices();
}
```
